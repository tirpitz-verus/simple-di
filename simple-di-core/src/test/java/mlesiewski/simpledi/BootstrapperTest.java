package mlesiewski.simpledi;

import mlesiewski.simpledi.annotations.Registerable;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class BootstrapperTest implements Registerable {

    private static boolean registered;

    @Test
    public void bootstrapRegistersRegisterables() throws Exception {
        // given
        Bootstrapper.bootstrapped = false;
        // when
        Bootstrapper.bootstrap();
        // then
        assertTrue(registered, "Bootstrapper.bootstrap() did not call BootstrapperTest#register()");
    }

    @BeforeMethod
    public void setUp() throws Exception {
        registered = false;
    }

    @Override
    public void register() {
        registered = true;
    }
}