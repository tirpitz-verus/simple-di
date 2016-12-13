package mlesiewski.simpledi.apt.model;

import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * An entity representing a class implementing BeanProvider interface.
 */
public class BeanProviderEntity extends ClassEntity implements GeneratedCode {

    private final BeanEntity beanEntity;
    private final TypeElement source;

    /**
     * @param beanEntity a bean that is going to be provided by this entity
     * @param simpleNamePostfix postfix appended to the represented class simple name
     */
    protected BeanProviderEntity(BeanEntity beanEntity, String simpleNamePostfix, TypeElement source) {
        super(beanEntity.packageName(), simpleNameOf(beanEntity, simpleNamePostfix));
        this.beanEntity = beanEntity;
        this.source = source;
    }

    /**
     * @param beanEntity a bean that is going to be provided by this entity
     */
    public BeanProviderEntity(BeanEntity beanEntity, TypeElement source) {
        super(beanEntity.packageName(), simpleNameOf(beanEntity, "Provider"));
        this.beanEntity = beanEntity;
        this.source = source;
    }

    /**
     * @return a bean that is going to be provided by this entity
     */
    public BeanEntity beanEntity() {
        return this.beanEntity;
    }

    /**
     * @return name of the provided {@link BeanEntity}.
     */
    public BeanName beanName() {
        return beanEntity().beanName();
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasSource() {
        return source != null;
    }

    /** {@inheritDoc} */
    @Override
    public TypeElement getSource() {
        return source;
    }

    /** @return set of hard (constructor injected) dependencies */
    public Set<BeanName> hardDependencies() {
        return beanEntity.hardDependencies();
    }

    /**
     * @return simple name for the provider implementation represented by this entity
     */
    private static String simpleNameOf(BeanEntity producedBean, String simpleNamePostfix) {
        String name = producedBean.defaultName() ? producedBean.simpleName().replaceAll(producedBean.packageName() + "\\.", "") : producedBean.name();
        String scope = producedBean.defaultScope() ? "" : producedBean.scope();
        return name + scope.replaceAll("\\.", "_") + simpleNamePostfix;
    }

}
