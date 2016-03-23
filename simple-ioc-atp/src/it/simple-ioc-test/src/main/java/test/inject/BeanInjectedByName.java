package test.inject;

import java.util.UUID;

public class BeanInjectedByName {

    public static final UUID CALL_UUID = UUID.randomUUID();

    public UUID call() {
        return CALL_UUID;
    }
}
