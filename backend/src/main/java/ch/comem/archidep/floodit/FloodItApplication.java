package ch.comem.archidep.floodit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableAutoConfiguration
@EnableConfigurationProperties
@EntityScan(basePackages = { "ch.comem.archidep.floodit" })
@SpringBootApplication(scanBasePackages = "ch.comem.archidep.floodit")
public class FloodItApplication {

  public static void main(String[] args) {
    SpringApplication.run(FloodItApplication.class, args);
  }
}
