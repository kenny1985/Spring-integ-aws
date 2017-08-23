package org.springframework.integration.aws;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SampleCommandLineRunner implements CommandLineRunner {

    private final SampleMessageGateway gateway;

    public SampleCommandLineRunner(SampleMessageGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public void run(String... args) throws Exception {
        for (String arg : args) {
            this.gateway.echo(arg);
        }
    }

}
