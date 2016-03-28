package mlesiewski.simpleioc;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class TemplateTest {

    @Test
    public void compilesTemplates() throws Exception {
        // given
        StringBuilder builder = new StringBuilder("text text {{keyWord}} text");
        Template template = new Template(builder);
        Map<String, String> input = new HashMap<>();
        input.put("keyWord", "text");
        // when
        String actual = template.compile(input);
        // then
        assertEquals("text text text text", actual);
    }
}