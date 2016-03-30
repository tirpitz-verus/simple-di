package test.inject;

import java.util.UUID;

public class BeanInjectedByType {

    public static final UUID CALL_UUID = UUID.randomUUID();

    public UUID call() {
        return CALL_UUID;
    }
}
