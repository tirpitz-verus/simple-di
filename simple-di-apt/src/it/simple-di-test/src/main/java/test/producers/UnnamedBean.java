package test.producers;

import mlesiewski.simpledi.annotations.Bean;

@Bean
public class UnnamedBean {

    public UnnamedBean(String name) {
        this.name = name;
    }

    public String name;
}
