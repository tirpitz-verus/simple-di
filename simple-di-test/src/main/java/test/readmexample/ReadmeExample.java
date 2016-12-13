package test.readmexample;

import mlesiewski.simpledi.core.BeanRegistry;
import mlesiewski.simpledi.core.annotations.Inject;
import mlesiewski.simpledi.core.annotations.Produce;
import mlesiewski.simpledi.core.scopes.NewInstanceScope;

class A {}

class MakerOfA {

    private static final A SINGLETON_A = new A();
    @Produce(scope = NewInstanceScope.NAME)
    A makeA () { return new A(); }

    @Produce(name = "singleton_a")
    A getA () { return SINGLETON_A; }

}

class B {

    @Inject(name = "singleton_a")
    A a1;
    final A a2;

    B (@Inject(scope = NewInstanceScope.NAME) A a2) { this.a2 = a2; }

}

class C {

    B b;
    @Inject
    C (B b) { this.b = b; }

}

public class ReadmeExample {

    public void runReadmeExample() throws Exception {
        C c = BeanRegistry.getBean(C.class);
        System.out.println("great success!!");
    }
}
