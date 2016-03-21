package test;

import mlesiewski.simpleioc.Produce;

public class Test {

    @Produce
    public Test makeABean() {
        return new Test();
    }

}