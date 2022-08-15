package ch.comem.archidep.floodit.utils;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public abstract class AbstractServiceTests {

  @Autowired
  private DatabaseTestUtils databaseTestUtils;

  @Autowired
  private TransactionTemplate transactionTemplate;

  @BeforeEach
  private void cleanUpDatabase() {
    this.databaseTestUtils.resetDatabase();
  }

  protected void transactionWithoutResult(Runnable runnable) {
    this.transactionTemplate.executeWithoutResult(status -> runnable.run());
  }
}
