package ch.comem.archidep.floodit.games;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

import ch.comem.archidep.floodit.games.data.CreateGameDto;
import ch.comem.archidep.floodit.utils.AbstractServiceTests;
import ch.comem.archidep.floodit.utils.TestUtils;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class GameServiceTests extends AbstractServiceTests {

  @Autowired
  private GameService gameService;

  @Test
  void create_a_game() {
    var dto = new CreateGameDto("John Doe", 30, 30, 6);

    var now = LocalDateTime.now();
    var result = this.gameService.createGame(dto);

    assertThat(result, isA(Game.class));
    assertThat(result.getId(), is(greaterThanOrEqualTo(1L)));
    assertThat(result.getPlayerName(), is(equalTo("John Doe")));
    assertThat(result.getBoardWidth(), is(equalTo(30)));
    assertThat(result.getBoardHeight(), is(equalTo(30)));
    assertThat(result.getNumberOfColors(), is(equalTo(6)));
    assertThat(
      result.getSeed(),
      is(
        allOf(
          greaterThanOrEqualTo(Long.MIN_VALUE),
          lessThanOrEqualTo(Long.MAX_VALUE)
        )
      )
    );
    assertThat(
      result.getCreatedAt(),
      is(allOf(greaterThanOrEqualTo(now), lessThan(now.plusSeconds(3))))
    );
    assertThat(result.getVersion(), is(equalTo(0L)));
    assertThat(
      TestUtils.getFieldNames(result),
      containsInAnyOrder(
        "id",
        "playerName",
        "boardWidth",
        "boardHeight",
        "numberOfColors",
        "seed",
        "createdAt",
        "version"
      )
    );
  }
}
