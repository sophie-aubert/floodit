package ch.comem.archidep.floodit.games;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.lessThan;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import ch.comem.archidep.floodit.utils.TestUtils;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class GameServiceTests {

  @Autowired
  private GameService gameService;

  @Test
  void create_a_game() {
    var now = LocalDateTime.now();
    var result = this.gameService.createGame();

    assertThat(result, isA(Game.class));
    assertThat(result.getId(), is(greaterThanOrEqualTo(1L)));
    assertThat(result.getCreatedAt(), is(allOf(greaterThanOrEqualTo(now), lessThan(now.plusSeconds(3)))));
    assertThat(result.getVersion(), is(equalTo(0L)));
    assertThat(TestUtils.getFieldNames(result), containsInAnyOrder("id", "createdAt", "version"));
  }
}
