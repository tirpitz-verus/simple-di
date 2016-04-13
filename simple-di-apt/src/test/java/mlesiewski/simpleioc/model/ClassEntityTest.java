package mlesiewski.simpleioc.model;

import org.testng.annotations.Test;

import javax.lang.model.type.TypeMirror;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClassEntityTest {

    @Test
    public void correctlySettsPackageAndSimpleName() throws Exception {
        // given
        String classPackage = "com.example.class.package";
        String simpleName = "ClassName";
        String typeName = classPackage + "." + simpleName;
        // when
        TypeMirror typeMirror = getTypeMirror(typeName);
        ClassEntity classEntity = ClassEntity.from(typeMirror);
        // then
        assertThat(classEntity.packageName(), equalTo(classPackage));
        assertThat(classEntity.simpleName(), equalTo(simpleName));
        assertThat(classEntity.typeName(), equalTo(typeName));
    }

    private TypeMirror getTypeMirror(String classFullName) {
        TypeMirror mock = mock(TypeMirror.class);
        when(mock.toString()).thenReturn(classFullName);
        return mock;
    }
}