package ch.comem.archidep.floodit.games;

import ch.comem.archidep.floodit.games.data.CreateGameDto;
import ch.comem.archidep.floodit.games.data.CreatedGameDto;
import ch.comem.archidep.floodit.games.data.GameDto;
import ch.comem.archidep.floodit.games.data.MoveDto;
import ch.comem.archidep.floodit.games.data.PlayDto;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class GameService {

  private static final Logger LOGGER = LoggerFactory.getLogger(
    GameService.class
  );

  @Autowired
  private GameRepository gameRepository;

  @Autowired
  private MoveRepository moveRepository;

  public CreatedGameDto createGame(CreateGameDto dto) {
    var game = new Game(dto);

    var createdGame = this.gameRepository.saveAndFlush(game);

    LOGGER.info("Created game {}", createdGame);

    return createdGame.toCreatedDto();
  }

  public List<GameDto> listRecentGames() {
    return this.gameRepository.findTop10ByOrderByCreatedAtDesc()
      .stream()
      .map(Game::toDto)
      .toList();
  }

  public GameDto getGame(long id) {
    return this.gameRepository.findById(id)
      .map(Game::toDto)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }

  public MoveDto play(PlayDto dto) {
    var game = this.loadGame(dto.gameId());

    var board = game.buildCurrentBoard();
    var flooded = board.flood(dto.color());

    var newState = GameState.ONGOING;
    if (board.isOneColor()) {
      newState = GameState.WIN;
    } else if (game.getMoves().size() + 1 == game.getMaxMoves()) {
      newState = GameState.LOSS;
    }

    var newMove = new Move(game, dto.color());
    game.getMoves().add(newMove);

    var createdMove = this.moveRepository.saveAndFlush(newMove);

    game.setState(newState);
    game.touch();
    this.gameRepository.saveAndFlush(game);

    LOGGER.info("Created move {} for game {}", createdMove, game);

    return createdMove.toDto(flooded, newState);
  }

  private Game loadGame(Long gameId) {
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
