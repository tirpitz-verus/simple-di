# simple-di [![Build Status](https://travis-ci.org/tirpitz-verus/simple-di.svg?branch=master)](https://travis-ci.org/tirpitz-verus/simple-di) [![codebeat badge](https://codebeat.co/badges/a6bdd2ee-3a13-4469-847b-286fa71cc52f)](https://codebeat.co/projects/github-com-tirpitz-verus-simple-di) [![Libraries.io for GitHub](https://img.shields.io/badge/license-MIT-blue.svg)](https://github.com/tirpitz-verus/simple-di/blob/master/LICENSE)
a very simple dependency injection framework for Java 8

current version `1.0-SNAPSHOT`

## 1. What is simple-di?
_simple-di_ is a very simple dependency injection framework for Java 8.
That means that some of the more advanced features (that can be found in other full fledged dependency injection frameworks) won't be found here.
The functionalities that _simple-di_ provides should be easy to use and simple to understand.
The runtime dependencies were kept to a minimum as well.
That is why [JSR-330](https://www.jcp.org/en/jsr/detail?id=330) annotations we not used.
(I'm yet to see a single project that changed a dependency injection framework during its lifetime.)
Not only I did not want _simple-di_ to depend on it but it did not match the simplistic philosophy of _simple-di_. 

### 1.1 tldr
```` 
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
````

### 1.2 how it works?
_simple-di_ tries not to use Java Reflection API.
It's not because Reflection API is bad.
It's great.
(And more so in Java 8!)

_simple-di_ generates a set of Java classes that will provide (actually they are called **Providers**) required dependencies (**Beans**) to be used by the engine.
Those classes will be generated during annotation processing phase of the compilation.
That way _simple-di_ can build dependency graphs and configure itself as a code.
Also it's fun.

At runtime each time the framework is asked to provide a bean instance it will find a suitable **Scope**.
That scope will find an appropriate **Bean Provider** and ask it for the bean instance.
Providers are also required to ask the framework for the dependencies needed to construct the bean.
So it will trigger a search for another bean provider.

#### 1.2.1 Beans

Beans are recognized by their **Bean Names**.
The name will be a String assigned to the bean or its Type (as a String).
It also consists of a scope name.
If no scope name is assigned than default one is assumed.

Beans have dependencies.
Some of them are required during object instantiation (aka the constructor dependencies) and are called **Hard Dependencies**.
In their case cycles in the dependency graph are not allowed.
The other type are **Soft Dependencies** and these are injected into fields (using setters if the field is not accessible by the bean provider).

Beans are recognized during annotation processing by:
* a class level **@Bean** annotation
* they are being used as dependencies through a use of **@Inject** annotation
* they have dependencies through a use of **@Inject** annotation
* they are being produced by a method annotated with a **@Produce** annotation
* they produce other beans by a method annotated with a **@Produce** annotation

#### 1.2.2 custom Providers

It is also possible to register a custom bean provider during the runtime (**BeanRegistry** has a method for that).
Your class would need to implement the **BeanProvider** interface.

#### 1.2.3 Bean producers

A special type of a bean is a one that is being instantiated (produced) by your own code.
The **@Produce** annotation marks a method that produces beans.
Under the hood the class with such a method becomes a bean provided by a special producer bean provider.

#### 1.2.4 Scopes

Actually I'm not so sure that the name **Scope** is such a good idea in the context of _simple-di_ simplicity but every other dependency injection framework uses that.
A scope is simply a hash map of bean providers that has a defined strategy how to deal with the providers and the stuff that they provide.
A "lifecycle" might be a better name.  
Each time **BeanRegistry** is asked for a bean it gets the scope name from the beans name of the bean requested.

Currently there are only three **Scope** implementations:
* **ApplicationScope** - Also called the "eager" in javadoc - it eagerly instantiates all of its beans (during bootstrap) and keeps those instances forever (in JVM terms). These instances are effectively singletons. Please don't let beans from other scopes have hard dependencies of eagerly created beans.
* **SingletonScope** - Instantiates singleton beans lazily and throws them away if they are not needed.
* **NewInstanceScope** - Always gives you a new instance of a bean and does not retain it.

SingletonScope will be used if no scope is provided.
`BeanRegistry.defaultScope()` will return its name.

#### 1.2.5 custom scopes

It is possible to implement a custom **Scope** implementation and use it in the same way as other scopes are being used.

#### 1.2.6 bootstrap

**BeanRegistry** is bootstraped statically when the application starts.
It uses the [ServiceLoader](http://docs.oracle.com/javase/8/docs/api/java/util/ServiceLoader.html).
During this phase:
1. non-custom scopes are instantiated and started
1. custom scopes are loaded
1. providers are loaded and registered with correct scopes

### 1.3 exception handling
_simple-di_ does not throw checked exceptions and wraps them in its own unchecked exception class **SimpleDiException**.

### 1.4 logs
_simple-di_ uses [Slf4j](http://www.slf4j.org/) during runtime (the only runtime dependency) and a standard Java annotation processing output during annotation processing (duh!).

## 2. What simple-di is not?
The philosophy behind _simple-di_ is very simple - "to have a way to inject an uniquely named instance".
It is agnostic of application servers and containers.
It does not understand transactions nor requests.
And it's just a pet project.

If You need some more fancy stuff that get one of these:
* [Spring](https://projects.spring.io/spring-framework/)
* [Guice](https://github.com/google/guice)
* [Weld](http://weld.cdi-spec.org/)
* [Dagger](http://square.github.io/dagger/)
* [PicoContainer](http://picocontainer.com/)
* ... there might be even more of these

## 3. Motivation
A motivation behind this project is to take on the complexity of a dependency injection framework and see why others do this stuff the way they do it.
Writing my own annotation processor was an added bonus.
I considered using it for small Java SE apps (basically other pet projects).

## 4. How do I use it?
It's all fine and dandy but You would like to take _simple-di_ for a spin.
How do you go about it?
I hope that while a proper releases are still to come this stuff isn't complicated.

### 4.1 obtain the jars
First things first.
Currently You'll need to build it Yourself.
But that shouldn't be too hard.

To run this project You will need:
* sources on Your machine (checked out from GitHub)
* Java 8
* Maven 3
* `mvn package`
* You could even `mvn install` if You use Maven to build Your project. The **simple-di-test** module can be used as an example usage.

### 4.2 compile Your code
* **simple-di-core** module is the runtime dependency
* **simple-di-apt** module is the annotation processor that will process the annotations of Your code

### 4.3 use the API
You probably already guessed it but somewhere in Your code You will need to call (probably in Your `main()` method) the `BeanRegistry.getBean()` the get the first bean instance.
Go from there and let the framework instantiate other objects for You.

Another method is to use `MemberInjector.injectMembersInto()` which will not instantiate the bean for You but it will inject dependencies into annotated fields of an existing instance (it needs not to be a proper bean).
That could also be used for testing.

A more concrete examples of how to use _simple-di_ can be found in the **simple-di-test** module (which is just a set of integration tests).

## 5. I want to contribute
Please be my guest.
I haven't thought about it so I don't have a proper process in place.
Nonetheless I am sure that we'll figure this stuff out!

## 6. Plans for the future

Stuff I want to have before 1.0.0:
* make this library concurrency friendly
* process annotations of the base classes (aka be polymorphism aware)

Dreams for the future:
* injecting implementations into supertypes (eg. interfaces)
* don't wrap bean producers in providers if they have only one produce method
* bundle providers from the same package into one class
* bundle providers of public bean classes into one class
* don't create services to load if there is no code to generate
* verify cross-scope hard dependencies (probably it would mean that a notion of eager scope needs to be introduced)
* use proxies (to have a bit of aspect oriented functionality)
* inject dependencies into producer methods
* have some tooling for unit test frameworks

## 7. License
* [MIT License](http://www.opensource.org/licenses/mit-license.php)