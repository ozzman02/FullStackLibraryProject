package com.luv2code.library.config;

import com.luv2code.library.entity.Book;
import com.luv2code.library.entity.Message;
import com.luv2code.library.entity.Review;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    private static final String ALLOWED_ORIGINS = "http://localhost:3000";

    private static final HttpMethod[] UNSUPPORTED_ACTIONS = {
            HttpMethod.POST,
            HttpMethod.PATCH,
            HttpMethod.DELETE,
            HttpMethod.PUT
    };

    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration configuration,
                                                     CorsRegistry corsRegistry) {
        configuration.exposeIdsFor(Book.class);
        configuration.exposeIdsFor(Review.class);
        configuration.exposeIdsFor(Message.class);

        disableHttpMethods(Book.class, configuration);
        disableHttpMethods(Review.class, configuration);
        disableHttpMethods(Message.class, configuration);

        corsRegistry.addMapping(configuration.getBasePath() + "/**")
                .allowedOrigins(ALLOWED_ORIGINS);
    }

    private void disableHttpMethods(Class<?> theClass, RepositoryRestConfiguration repositoryRestConfiguration) {
        repositoryRestConfiguration.getExposureConfiguration()
                .forDomainType(theClass)
                .withItemExposure(((metadata, httpMethods) ->
                        httpMethods.disable(MyDataRestConfig.UNSUPPORTED_ACTIONS)))
                .withCollectionExposure((metadata, httpMethods) ->
                        httpMethods.disable(MyDataRestConfig.UNSUPPORTED_ACTIONS));
    }

}
