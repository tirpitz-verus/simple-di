package mlesiewski.simpledi.apt.template;

import mlesiewski.simpledi.apt.SimpleDiAptException;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TemplateTest {

    @Test
    public void compilesTemplates() throws Exception {
        // given
        Template template = new Template("{{keyWord1}} text {{keyWord1}} text {{keyWord2}}");
        Map<String, String> input = new HashMap<>();
        input.put("keyWord1", "text");
        input.put("keyWord2", "text");
        // when
        String actual = template.compile(input);
        // then
        assertThat(actual, is("text text text text text"));
    }

    @Test(expectedExceptions = SimpleDiAptException.class)
    public void throwsErrorOnUnusedKey() throws Exception {
        // given
        Template template = new Template("{{keyWord}} text {{keyWord}} text");
        Map<String, String> input = new HashMap<>();
        // when
        template.compile(input);
        // then - error
    }

    @Test(expectedExceptions = SimpleDiAptException.class)
    public void throwsErrorOnTooManyValues() throws Exception {
        // given
        Template template = new Template("{{keyWord}} text {{keyWord}} text");
        Map<String, String> input = new HashMap<>();
        input.put("keyWord", "text");
        input.put("oneValueTooMany", "text");
        // when
        template.compile(input);
        // then - error
    }

    @Test(expectedExceptions = SimpleDiAptException.class)
    public void throwsErrorOnEmptyKey() throws Exception {
        // when
        new Template("text text {{}} text");
        // then - error
    }
}