package mlesiewski.simpledi.apt.model;

import mlesiewski.simpledi.apt.SimpleDiAptException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.lang.model.element.TypeElement;

import static mlesiewski.simpledi.apt.model.GeneratedOrderMatcher.returnsRegistrableInOrder;
import static org.hamcrest.MatcherAssert.assertThat;

public class GeneratedCodeCollectorTest {

    private GeneratedCodeCollector collector;

    @Test
    public void returnsManyDependendantsInTopologicalOrder() throws Exception {
        // given
        BeanEntity son = createBeanEntity("son");
        BeanEntity daughter = createBeanEntity("daughter");
        BeanEntity father = createBeanEntity("father");
        // when
        son.hardDependency(father.beanName());
        daughter.hardDependency(father.beanName());
        // then
        assertThat(collector, returnsRegistrableInOrder(father, son, daughter));
    }

    @Test
    public void returnsLinearDependenciesRegistrableInTopologicalOrder() throws Exception {
        // given
        BeanEntity son = createBeanEntity("son");
        BeanEntity father = createBeanEntity("father");
        BeanEntity grandpa = createBeanEntity("grandpa");
        // when
        son.hardDependency(father.beanName());
        father.hardDependency(grandpa.beanName());
        // then
        assertThat(collector, returnsRegistrableInOrder(grandpa, father, son));
    }

    @Test
    public void returnsSimpleTreeDependenciesRegistrableInTopologicalOrder() throws Exception {
        // given
        BeanEntity son = createBeanEntity("son");
        BeanEntity mother = createBeanEntity("mother");
        BeanEntity father = createBeanEntity("father");
        // when
        son.hardDependency(mother.beanName());
        son.hardDependency(father.beanName());
        // then
        assertThat(collector, returnsRegistrableInOrder(mother, father, son));
    }

    @Test
    public void returnsBranchedTreeDependenciesRegistrableInTopologicalOrder() throws Exception {
        // given
        BeanEntity son = createBeanEntity("son");
        BeanEntity father = createBeanEntity("father");
        BeanEntity fathersFather = createBeanEntity("fathersFather");
        BeanEntity fathersMother = createBeanEntity("fathersMother");
        BeanEntity mother = createBeanEntity("mother");
        // when
        son.hardDependency(father.beanName());
        father.hardDependency(fathersFather.beanName());
        father.hardDependency(fathersMother.beanName());
        son.hardDependency(mother.beanName());
        // then
        assertThat(collector, returnsRegistrableInOrder(mother, fathersMother, fathersFather, father, son));
    }

    @Test(expectedExceptions = SimpleDiAptException.class, expectedExceptionsMessageRegExp = ".*cycle.*")
    public void throwsExceptionOnAHardCycle() throws Exception {
        // given
        BeanEntity entity1 = createBeanEntity("entity1");
        BeanEntity entity2 = createBeanEntity("entity2");
        entity1.hardDependency(entity2.beanName());
        entity2.hardDependency(entity1.beanName());
        // when
        collector.registrable();
        // then - error
    }

    @BeforeMethod
    public void initCut() {
        collector = new GeneratedCodeCollector();
    }

    private BeanEntity createBeanEntity(String name) {
        ClassEntity classEntity = new ClassEntity("", name);
        BeanEntity beanEntity = BeanEntity.builder().from(classEntity).withName(name).build();
        TypeElement source = null;
        BeanProviderEntity providerEntity = new BeanProviderEntity(beanEntity, source);
        collector.registrable(providerEntity);
        return beanEntity;
    }
}
