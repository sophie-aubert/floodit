package ch.comem.archidep.floodit.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseSchemaUtils {

  private static final Path CREATE_SCHEMA_FILE = Path.of(
    "src/main/resources/db/create.sql"
  );

  @Bean
  public static BeanFactoryPostProcessor cleanUpDatabaseSchemaFiles() {
    return beanFactoryPostProcessor -> {
      try {
        Files.write(
          CREATE_SCHEMA_FILE,
          new byte[] {},
          StandardOpenOption.TRUNCATE_EXISTING
        );
      } catch (IOException e) {
        throw new IllegalStateException(e);
      }
    };
  }
}
