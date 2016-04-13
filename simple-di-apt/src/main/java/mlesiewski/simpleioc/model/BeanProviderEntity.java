package mlesiewski.simpleioc.model;

/**
 * An entity representing a class implementing BeanProvider interface.
 */
public class BeanProviderEntity extends ClassEntity implements CodeGenerated {

    private final BeanEntity beanEntity;

    /**
     * @param packageName package name of the represented class
     * @param simpleName simple name of the represented class
     * @param beanEntity a bean that is going to be provided by this entity
     */
    protected BeanProviderEntity(String packageName, String simpleName, BeanEntity beanEntity) {
        super(packageName, simpleName);
        this.beanEntity = beanEntity;
    }

    /**
     * @param beanEntity a bean that is going to be provided by this entity
     */
    public BeanProviderEntity(BeanEntity beanEntity) {
        super(beanEntity.packageName(), beanEntity.simpleName() + "Provider");
        this.beanEntity = beanEntity;
    }

    /**
     * @return a bean that is going to be provided by this entity
     */
    public BeanEntity beanEntity() {
        return this.beanEntity;
    }
}
