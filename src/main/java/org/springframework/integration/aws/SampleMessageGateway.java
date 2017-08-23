package org.springframework.integration.aws;

import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(defaultRequestChannel = "outputChannel")
public interface SampleMessageGateway {

    void echo(String message);

}
