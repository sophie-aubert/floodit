package ch.comem.archidep.floodit.games;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isA;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class GameServiceTests {

  @Autowired
  private GameService gameService;

  @Test
  void create_a_game() {
    var result = this.gameService.createGame();

    assertThat(result, isA(Game.class));
  }
}
