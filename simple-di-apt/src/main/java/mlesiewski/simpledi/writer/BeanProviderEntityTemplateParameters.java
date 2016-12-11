package mlesiewski.simpledi.writer;

import mlesiewski.simpledi.model.BeanEntity;
import mlesiewski.simpledi.model.BeanProviderEntity;
import mlesiewski.simpledi.model.GeneratedCode;

import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * represents template parameters for {@link BeanProviderEntity}
 */
class BeanProviderEntityTemplateParameters extends HashMap<String, String>{

    public BeanProviderEntityTemplateParameters(GeneratedCode generated) {
        BeanProviderEntity entity = (BeanProviderEntity) generated;
        put("beanProviderPackage", entity.packageName());
        put("beanProviderSimpleName", entity.simpleName());
        BeanEntity beanEntity = entity.beanEntity();
        put("beanType", beanEntity.typeName());
        put("beanName", beanEntity.name());
        put("beanScope", beanEntity.scope());
        put("constructorArguments", getConstructorArguments(beanEntity));
        StringBuilder softDependencies = getSoftDependencies(beanEntity);
        put("softDependencies", softDependencies.length() != 0 ? softDependencies.toString() : "//empty");
    }

    private String getConstructorArguments(BeanEntity beanEntity) {
        return beanEntity.constructor().list().stream()
                .map(beanName -> {
                    if (beanName.scopeIsDefault()) {
                        return String.format("BeanRegistry.getBean(\"%s\")", beanName.name());
                    } else {
                        return String.format("BeanRegistry.getBean(\"%s\", \"%s\")", beanName.name(), beanName.scope());
                    }
                })
                .collect(Collectors.joining(", "));
    }

    private StringBuilder getSoftDependencies(BeanEntity beanEntity) {
        StringBuilder softDependencies = new StringBuilder();
        appendFieldDependencies(beanEntity, softDependencies);
        appendSetterDependencies(beanEntity, softDependencies);
        return softDependencies;
    }

    private void appendFieldDependencies(BeanEntity beanEntity, StringBuilder softDependencies) {
        beanEntity.fields().forEach((field, dependency) -> {
            softDependencies.append("bean.").append(field).append(" = ");
            softDependencies.append("BeanRegistry.getBean(\"").append(dependency.name()).append("\"");
            if (!dependency.scopeIsDefault()) {
                softDependencies.append(", \"").append(dependency.scope()).append("\"");
            }
            softDependencies.append(")");
            softDependencies.append(";\n\t\t");
        });
    }

    private void appendSetterDependencies(BeanEntity beanEntity, StringBuilder softDependencies) {
        beanEntity.setters().forEach((setter, dependency) -> {
            softDependencies.append("bean.").append(setter).append("(");
            softDependencies.append("BeanRegistry.getBean(\"").append(dependency.name()).append("\"");
            if (!dependency.scopeIsDefault()) {
                softDependencies.append(", \"").append(dependency.scope()).append("\"");
            }
            softDependencies.append(")");
            softDependencies.append(");\n\t\t");
        });
    }
}
