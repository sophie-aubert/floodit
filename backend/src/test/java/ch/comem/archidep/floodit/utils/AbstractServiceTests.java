package ch.comem.archidep.floodit.utils;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public abstract class AbstractServiceTests {

  @Autowired
  private DatabaseTestUtils databaseTestUtils;

  @BeforeEach
  private void cleanUpDatabase() {
    this.databaseTestUtils.resetDatabase();
  }
}
