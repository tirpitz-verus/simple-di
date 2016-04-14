package mlesiewski.simpledi.model;

import javax.lang.model.type.TypeMirror;

/**
 * Base entity representing a class.
 */
public class ClassEntity {

    private final String packageName;
    private final String simpleName;
    private final String typeName;

    /**
     * @param packageName package name of the represented class
     * @param simpleName simple name of the represented class
     */
    ClassEntity(String packageName, String simpleName) {
        this.packageName = packageName;
        this.simpleName = simpleName;
        this.typeName = packageName + "." + simpleName;
    }

    /**
     * @return package name of the represented class
     */
    public String packageName() {
        return packageName;
    }

    /**
     * @return simple name of the represented class
     */
    public String simpleName() {
        return simpleName;
    }

    /**
     * @return name of the represented class
     */
    public String typeName() {
        return typeName;
    }

    /**
     * @param typeMirror type for the new class entity to be named after
     * @return new class entity that is named after the type mirror provided
     */
    public static ClassEntity from(TypeMirror typeMirror) {
        String typeName = typeMirror.toString();
        String classPackage = typeName.substring(0, typeName.lastIndexOf("."));
        String classSimpleName = typeName.substring(typeName.lastIndexOf(".") + 1);
        return new ClassEntity(classPackage, classSimpleName);
    }
}
