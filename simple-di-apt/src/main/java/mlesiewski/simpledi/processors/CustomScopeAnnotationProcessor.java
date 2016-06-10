package mlesiewski.simpledi.processors;

import mlesiewski.simpledi.Logger;
import mlesiewski.simpledi.SimpleDiAptException;
import mlesiewski.simpledi.annotations.CustomScope;
import mlesiewski.simpledi.model.GeneratedCodeCollector;
import mlesiewski.simpledi.scopes.Scope;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.*;

/**
 * Process @CustomScope annotations - just collects them.
 */
public class CustomScopeAnnotationProcessor {

    private LinkedList<String> names = new LinkedList<>();

    /** @param roundEnv environment to get annotated {@link Element Elements} from */
    public void process(RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(CustomScope.class).stream().forEach(this::processElement);
    }

    /**
     * @param element element to precess
     */
    void processElement(Element element) {
        Logger.note("processing element '" + element.getSimpleName() + "'");
        validate(element);
        names.add(element.asType().toString());
    }

    public Collection<String> scopes() {
        return names;
    }

    /**
     * @param element annotated element to validate - was the annotation applied to the right place?
     */
    void validate(Element element) {
        if (element.getKind() != ElementKind.CLASS) {
            throw new SimpleDiAptException(CustomScope.class.getName() + " is only applicable for classes", element);
        }
        Set<Modifier> modifiers = element.getModifiers();
        if (modifiers.contains(Modifier.ABSTRACT) || modifiers.contains(Modifier.PRIVATE) || modifiers.contains(Modifier.PROTECTED)) {
            throw new SimpleDiAptException(CustomScope.class.getName() + " is only applicable for non-abstract public classes", element);
        }
        String scopeInterfaceSimpleName = Scope.class.getSimpleName();
        TypeElement typeElement = (TypeElement) element;
        boolean implementsScope = getInterfaces(typeElement).stream()
                .map(anInterface -> (DeclaredType) anInterface)
                .map(anInterface -> (TypeElement) anInterface.asElement())
                .map(TypeElement::getSimpleName)
                .map(Name::toString)
                .anyMatch(scopeInterfaceSimpleName::equals);
        if (!implementsScope) {
            throw new SimpleDiAptException(CustomScope.class.getName() + " is only applicable for classes implementing " + scopeInterfaceSimpleName, element);
        }
    }

    private List<? extends TypeMirror> getInterfaces(TypeElement typeElement) {
        List<? extends TypeMirror> interfaces = typeElement.getInterfaces();
        ArrayList<? extends TypeMirror> result = new ArrayList<>(interfaces);
        TypeMirror superclass = typeElement.getSuperclass();
        if (superclass.getKind() == TypeKind.DECLARED) {
            DeclaredType declaredType = (DeclaredType) superclass;
            List superInterfaces = getInterfaces((TypeElement) declaredType.asElement());
            result.addAll(superInterfaces);
        }
        return result;
    }
}
