package ch.comem.archidep.floodit.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseSchemaUtils {

    @Bean
    public static BeanFactoryPostProcessor cleanUpDatabaseSchemaFiles() {
        return beanFactoryPostProcessor -> {
            try {
                Files.deleteIfExists(Path.of("src/main/resources/db/create.sql"));
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        };
    }
  }
