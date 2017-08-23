package org.springframework.integration.aws;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;

@ConfigurationProperties(prefix = "service", ignoreUnknownFields = false)
@ManagedResource
public class ServiceProperties {

    private String greeting = "Hello";

    @ManagedAttribute
    public String getGreeting() {
        return this.greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }

}
