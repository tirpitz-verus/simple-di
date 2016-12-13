package mlesiewski.simpledi.apt.processors;

import mlesiewski.simpledi.apt.Logger;
import mlesiewski.simpledi.apt.SimpleDiAptException;
import mlesiewski.simpledi.core.annotations.CustomScope;
import mlesiewski.simpledi.core.scopes.Scope;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Process @CustomScope annotations - just collects them.
 */
public class CustomScopeAnnotationProcessor {

    private LinkedList<String> names = new LinkedList<>();

    /** @param roundEnv environment to get annotated {@link Element Elements} from */
    public void process(RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(CustomScope.class).forEach(this::processElement);
    }

    /** @return names of all the valid elements processed */
    public Collection<String> scopes() {
        return names;
    }

    /**
     * @param element element to precess
     */
    private void processElement(Element element) {
        Logger.note("processing element '" + element.getSimpleName() + "'");
        validate(element);
        names.add(element.asType().toString());
    }

    /**
     * @param element annotated element to validate - was the annotation applied to the right place?
     */
    private void validate(Element element) {
        CustomScope annotation = element.getAnnotation(CustomScope.class);
        Validators.validBeanName(annotation.value(), CustomScope.class, element);
        Validators.validAccessibility(element, CustomScope.class, "classes");
        Validators.isAClass(element, CustomScope.class);
        Validators.validScopeConstructor((DeclaredType) element.asType());

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

    @SuppressWarnings("unchecked")
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
