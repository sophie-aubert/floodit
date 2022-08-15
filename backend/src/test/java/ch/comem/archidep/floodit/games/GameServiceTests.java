package ch.comem.archidep.floodit.games;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasLength;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

import ch.comem.archidep.floodit.business.Position;
import ch.comem.archidep.floodit.games.data.CreateGameDto;
import ch.comem.archidep.floodit.utils.AbstractServiceTests;
import ch.comem.archidep.floodit.utils.TestUtils;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class GameServiceTests extends AbstractServiceTests {

  @Autowired
  private GameFixtures gameFixtures;

  @Autowired
  private GameRepository gameRepository;

  @Autowired
  private GameService gameService;

  @Test
  void create_a_game() {
    var dto = new CreateGameDto("John Doe", 30, 30, 6, 25);

    var now = LocalDateTime.now();
    var result = this.gameService.createGame(dto);

    assertThat(result.id(), is(greaterThanOrEqualTo(1L)));
    assertThat(
      result.secret(),
      allOf(hasLength(255), not(blankOrNullString()))
    );
    assertThat(result.state(), is(GameState.ONGOING));
    assertThat(result.playerName(), is(equalTo("John Doe")));
    assertThat(result.boardWidth(), is(equalTo(30)));
    assertThat(result.boardHeight(), is(equalTo(30)));
    assertThat(result.numberOfColors(), is(equalTo(6)));
    assertThat(result.maxMoves(), is(equalTo(25)));
    assertThat(result.moves(), is(empty()));
    assertThat(
      result.createdAt(),
      is(allOf(greaterThanOrEqualTo(now), lessThan(now.plusSeconds(3))))
    );
    assertThat(
      result.updatedAt(),
      is(allOf(greaterThanOrEqualTo(now), lessThan(now.plusSeconds(3))))
    );
    assertThat(
      TestUtils.getFieldNames(result),
      containsInAnyOrder(
        "id",
        "secret",
        "state",
        "playerName",
        "boardWidth",
        "boardHeight",
        "numberOfColors",
        "maxMoves",
        "moves",
        "createdAt",
        "updatedAt"
      )
    );

    transactionWithoutResult(() -> {
      var createdGame = this.getGame(result.id());
      assertThat(createdGame.getSecret(), is(equalTo(result.secret())));
      assertThat(createdGame.getState(), is(equalTo(result.state())));
      assertThat(createdGame.getPlayerName(), is(equalTo(result.playerName())));
      assertThat(createdGame.getBoardWidth(), is(equalTo(result.boardWidth())));
      assertThat(
        createdGame.getBoardHeight(),
        is(equalTo(result.boardHeight()))
      );
      assertThat(
        createdGame.getNumberOfColors(),
        is(equalTo(result.numberOfColors()))
      );
      assertThat(createdGame.getMaxMoves(), is(equalTo(result.maxMoves())));
      assertThat(
        createdGame.getSeed(),
        is(
          allOf(
            greaterThanOrEqualTo(Long.MIN_VALUE),
            lessThanOrEqualTo(Long.MAX_VALUE)
          )
        )
      );
      assertThat(createdGame.getMoves(), is(empty()));
      assertThat(createdGame.getCreatedAt(), is(equalTo(result.createdAt())));
      assertThat(createdGame.getUpdatedAt(), is(equalTo(result.updatedAt())));
      assertThat(createdGame.getVersion(), is(equalTo(0L)));
      assertThat(
        TestUtils.getFieldNames(createdGame),
        containsInAnyOrder(
          "id",
          "secret",
          "state",
          "playerName",
          "boardWidth",
          "boardHeight",
          "numberOfColors",
          "maxMoves",
          "seed",
          "moves",
          "createdAt",
          "updatedAt",
          "version"
        )
      );
    });
  }

  @Test
  void play_the_first_move_in_a_game() {
    var game = this.gameFixtures.game(builder -> builder.withMaxMoves(3));
    var dto = GameFixtures.playMoveDto(game);

    var now = LocalDateTime.now();
    var result = this.gameService.play(dto);

    assertThat(result.id(), is(greaterThanOrEqualTo(1L)));
    assertThat(result.color(), is(equalTo(dto.color())));
    assertThat(result.flooded(), is(not(empty())));
    assertThat(result.gameState(), is(GameState.ONGOING));
    assertThat(
      result.createdAt(),
      is(allOf(greaterThanOrEqualTo(now), lessThan(now.plusSeconds(3))))
    );
    assertThat(
      TestUtils.getFieldNames(result),
      containsInAnyOrder("id", "color", "flooded", "gameState", "createdAt")
    );

    transactionWithoutResult(() -> {
      var updatedGame = this.getGame(game);

      assertThat(
        updatedGame.getUpdatedAt(),
        is(allOf(greaterThanOrEqualTo(now), lessThan(now.plusSeconds(3))))
      );

      var moves = updatedGame.getMoves();
      assertThat(moves, hasSize(1));

      var createdMove = updatedGame.getMoves().get(0);
      assertThat(createdMove, isA(Move.class));
      assertThat(createdMove.getId(), is(greaterThanOrEqualTo(1L)));
      assertThat(
        Optional
          .ofNullable(createdMove.getGame())
          .map(Game::getId)
          .orElse(null),
        is(equalTo(game.getId()))
      );
      assertThat(createdMove.getColor(), is(equalTo(dto.color())));
      assertThat(
        createdMove.getCreatedAt(),
        is(allOf(greaterThanOrEqualTo(now), lessThan(now.plusSeconds(3))))
      );
      assertThat(
        TestUtils.getFieldNames(createdMove),
        containsInAnyOrder("id", "game", "color", "createdAt")
      );
    });
  }

  @Test
  void win_a_3x3_game_in_2_moves() {
    // 2 2 2
    // 0 0 2
    // 0 1 1
    var game =
      this.gameFixtures.game(builder ->
          builder
            .withBoardWidth(3)
            .withBoardHeight(3)
            .withNumberOfColors(3)
            .withMaxMoves(3)
            .withSeed(42)
        );

    // 0 0 0
    // 0 0 0
    // 0 1 1
    var firstMoveDto = GameFixtures.playMoveDto(builder ->
      builder.withGame(game).withColor(0)
    );
    var firstMove = this.gameService.play(firstMoveDto);
    assertThat(
      firstMove.flooded(),
      containsInAnyOrder(
        Position.at(0, 0),
        Position.at(1, 0),
        Position.at(2, 0),
        Position.at(2, 1)
      )
    );
    assertThat(firstMove.gameState(), is(GameState.ONGOING));

    // 1 1 1
    // 1 1 1
    // 1 1 1
    var secondMoveDto = GameFixtures.playMoveDto(builder ->
      builder.withGame(game).withColor(1)
    );
    var secondMove = this.gameService.play(secondMoveDto);
    assertThat(
      secondMove.flooded(),
      containsInAnyOrder(
        Position.at(0, 1),
        Position.at(0, 2),
        Position.at(1, 1)
      )
    );
    assertThat(secondMove.gameState(), is(GameState.WIN));

    transactionWithoutResult(() -> {
      var updatedGame = this.getGame(game);
      assertThat(updatedGame.getState(), is(GameState.WIN));
    });
  }

  @Test
  void lose_a_3x3_game_in_3_moves() {
    // 2 2 2
    // 0 0 2
    // 0 1 1
    var game =
      this.gameFixtures.game(builder ->
          builder
            .withBoardWidth(3)
            .withBoardHeight(3)
            .withNumberOfColors(3)
            .withMaxMoves(3)
            .withSeed(42)
        );

    // 1 1 1
    // 0 0 1
    // 0 1 1
    var firstMoveDto = GameFixtures.playMoveDto(builder ->
      builder.withGame(game).withColor(1)
    );
    var firstMove = this.gameService.play(firstMoveDto);
    assertThat(
      firstMove.flooded(),
      containsInAnyOrder(
        Position.at(0, 0),
        Position.at(1, 0),
        Position.at(2, 0),
        Position.at(2, 1)
      )
    );
    assertThat(firstMove.gameState(), is(GameState.ONGOING));

    // 2 2 2
    // 0 0 2
    // 0 2 2
    var secondMoveDto = GameFixtures.playMoveDto(builder ->
      builder.withGame(game).withColor(2)
    );
    var secondMove = this.gameService.play(secondMoveDto);
    assertThat(
      secondMove.flooded(),
      containsInAnyOrder(Position.at(1, 2), Position.at(2, 2))
    );
    assertThat(secondMove.gameState(), is(GameState.ONGOING));

    // 1 1 1
    // 0 0 1
    // 0 1 1
    var thirdMoveDto = GameFixtures.playMoveDto(builder ->
      builder.withGame(game).withColor(1)
    );
    var thirdMove = this.gameService.play(thirdMoveDto);
    assertThat(thirdMove.flooded(), is(empty()));
    assertThat(thirdMove.gameState(), is(GameState.LOSS));

    transactionWithoutResult(() -> {
      var updatedGame = this.getGame(game);
      assertThat(updatedGame.getState(), is(GameState.LOSS));
    });
  }

  private Game getGame(Game game) {
    return this.getGame(game.getId());
  }

  private Game getGame(Long gameId) {
    return this.gameRepository.findById(gameId)
      .orElseThrow(() ->
        new IllegalStateException(
          String.format(
            "Could not find game with ID %s in the database",
            gameId
          )
        )
      );
  }
}
