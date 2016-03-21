package mlesiewski.simpleioc;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TemplateTest {

    @Test(dataProvider = "getTemplateNames")
    public void canLoadTemplates(String templateName) {
        // when
        Template.get(templateName);
        // then -- no error
    }

    @DataProvider
    public Object[][] getTemplateNames() {
        return new Object[][] {
                {"ProduceBeanProvider"}
        };
    }
}