package com.luv2code.library.config;

import com.luv2code.library.entity.Book;
import com.luv2code.library.entity.Message;
import com.luv2code.library.entity.Review;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import static com.luv2code.library.constants.ApplicationConstants.HTTPS_ALLOWED_ORIGINS;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration configuration,
                                                     CorsRegistry corsRegistry) {
        configuration.exposeIdsFor(Book.class);
        configuration.exposeIdsFor(Review.class);
        configuration.exposeIdsFor(Message.class);

        disableHttpMethods(Book.class, configuration);
        disableHttpMethods(Review.class, configuration);
        disableHttpMethods(Message.class, configuration);

        corsRegistry.addMapping(configuration.getBasePath() + "/**")
                .allowedOrigins(HTTPS_ALLOWED_ORIGINS);
    }

    private static final HttpMethod[] UNSUPPORTED_ACTIONS = {
            HttpMethod.POST,
            HttpMethod.PATCH,
            HttpMethod.DELETE,
            HttpMethod.PUT
    };

    private void disableHttpMethods(Class<?> theClass, RepositoryRestConfiguration repositoryRestConfiguration) {
        repositoryRestConfiguration.getExposureConfiguration()
                .forDomainType(theClass)
                .withItemExposure(((metadata, httpMethods) ->
                        httpMethods.disable(UNSUPPORTED_ACTIONS)))
                .withCollectionExposure((metadata, httpMethods) ->
                        httpMethods.disable(UNSUPPORTED_ACTIONS));
    }

}
