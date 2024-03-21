package myproject.resttemplate.resttemplate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.client.RestTemplateBuilderConfigurer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class RestTemplateBuilderConfig {

    @Value("${rest.template.rootUrl}")
    String rootUrl;

    @Value("${rest.template.username}")
    String username;

    @Value("${rest.template.password}")
    String password;


    //Wszędzie gdzie restTemplate będzie używać URI zostanie w pierwszej kolejności użyte to utworzone tutaj
    @Bean //Nadpisanie domyślnie buildera w springu
    RestTemplateBuilder restTemplateBuilder(RestTemplateBuilderConfigurer configurer) {

        assert rootUrl != null;


        // Utworzenie nowej instancji dla konfiguracji z dostępem do warości domyślnych springa
        RestTemplateBuilder builder = configurer.configure(new RestTemplateBuilder());

        DefaultUriBuilderFactory uriBuilderFactory =
                new DefaultUriBuilderFactory(rootUrl);

        /**
         * Napisanie builder.basicAuthentication("user1", "password") nie zadziała
         * należy utworzyć nową instancję z istniejącego już builder
         */
        RestTemplateBuilder builderWithAuth = builder.basicAuthentication(username, password);

        return builderWithAuth.uriTemplateHandler(uriBuilderFactory);
    }
}
