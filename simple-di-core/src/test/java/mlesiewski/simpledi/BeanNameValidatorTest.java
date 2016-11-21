package mlesiewski.simpledi;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class BeanNameValidatorTest {

    private BeanNameValidator cut = new BeanNameValidator();

    @Test(dataProvider = "data")
    public void testIsAValidName(String name, Boolean isValid) throws Exception {
        assertEquals(cut.isAValidName(name), isValid.booleanValue(), "wrong result for name'" + name + "'");
    }

    @DataProvider
    public static Object[][] data() {
        return new Object[][]{
                {null, false},
                {"", false},
                {"aa~", false},
                {"aa%^%^", false},
                {"              ", false},
                {"a.a", false},
                {"Fine-Name_it_15", true},
                {"aaaa", true},
                {"AAAA", true},
                {"----", true},
                {"____", true},
                {"1111", true},
        };
    }
}