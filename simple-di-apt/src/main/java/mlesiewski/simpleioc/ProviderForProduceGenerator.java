package mlesiewski.simpleioc;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import java.util.HashMap;
import java.util.Set;

public class ProviderForProduceGenerator extends BaseGenerator {

    private String returnTypeName;
    private String beanProviderSimpleName;
    private String beanProviderPackage;
    private String producerTypeName;
    private String producerMethodName;

    public ProviderForProduceGenerator(Element element) {
        super(element);
    }

    @Override
    public void validate() {
        if (element.getKind() != ElementKind.METHOD) {
            throw new SimpleIocAptException("mlesiewski.simpleioc.Produce is only applicable for methods", element);
        }
        Set<Modifier> modifiers = element.getModifiers();
        if (modifiers.contains(Modifier.ABSTRACT)) {
            throw new SimpleIocAptException("mlesiewski.simpleioc.Produce is only applicable for non-abstract methods", element);
        }
        if (!modifiers.contains(Modifier.PUBLIC)) {
            throw new SimpleIocAptException("mlesiewski.simpleioc.Produce is only applicable public methods", element);
        }
        if (modifiers.contains(Modifier.STATIC)) {
            throw new SimpleIocAptException("mlesiewski.simpleioc.Produce is only applicable for non-static methods", element);
        }
    }

    @Override
    public String newClassName() {
        ExecutableElement method = (ExecutableElement) element;
        TypeMirror returnType = method.getReturnType();
        returnTypeName = returnType.toString();
        String beanProviderName = returnTypeName + "BeanProvider";
        int latDot = beanProviderName.lastIndexOf(".");
        beanProviderSimpleName = beanProviderName.substring(latDot + 1);
        beanProviderPackage = beanProviderName.substring(0, latDot - 1);
        producerTypeName = element.getEnclosingElement().asType().toString();
        producerMethodName = element.getSimpleName().toString();
        return beanProviderName;
    }

    @Override
    public String getSourceCodeText() {
        Template produceBeanProvider = TemplateFactory.get("ProduceBeanProvider");
        HashMap<String, String> params = new HashMap<>();
        params.put("returnTypeName", returnTypeName);
        params.put("beanProviderSimpleName", beanProviderSimpleName);
        params.put("beanProviderPackage", beanProviderPackage);
        params.put("producerTypeName", producerTypeName);
        params.put("producerMethodName", producerMethodName);
        return produceBeanProvider.compile(params);
    }

}
