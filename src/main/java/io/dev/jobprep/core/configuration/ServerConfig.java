package io.dev.jobprep.core.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerConfig {

    @Value("${server.port}")
    private int httpsPort;

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addAdditionalTomcatConnectors(createHttpConnector(80));
        tomcat.addAdditionalTomcatConnectors(createHttpConnector(8080));
        return tomcat;
    }

    private Connector createHttpConnector(int port) {
        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        connector.setPort(port);
        connector.setSecure(false);
        connector.setRedirectPort(httpsPort);
        return connector;
    }
}