package mlesiewski.simpledi.model;

import mlesiewski.simpledi.SimpleDiAptException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static mlesiewski.simpledi.model.GeneratedOrderMatcher.returnsRegistrableInOrder;
import static org.hamcrest.MatcherAssert.assertThat;

public class GeneratedCodeCollectorTest {

    private GeneratedCodeCollector collector;

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
        assertThat(collector, returnsRegistrableInOrder(father, mother, son));
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
        assertThat(collector, returnsRegistrableInOrder(fathersMother, fathersFather, father, mother, son));
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
        ClassEntity class1 = new ClassEntity("", name);
        BeanEntity entity1 = BeanEntity.builder().from(class1).withName(name).build();
        BeanProviderEntity provider1 = new BeanProviderEntity(entity1);
        collector.registrable(provider1);
        return entity1;
    }
}