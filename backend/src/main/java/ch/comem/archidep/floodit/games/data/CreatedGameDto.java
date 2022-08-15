package ch.comem.archidep.floodit.games.data;

import ch.comem.archidep.floodit.games.GameState;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class CreatedGameDto extends GameDto {

  private final String secret;

  public CreatedGameDto(
    long id,
    GameState state,
    String playerName,
    int boardWidth,
    int boardHeight,
    int numberOfColors,
    int maxMoves,
    List<MoveDto> moves,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    String secret
  ) {
    super(
      id,
      state,
      playerName,
      boardWidth,
      boardHeight,
      numberOfColors,
      maxMoves,
      moves,
      createdAt,
      updatedAt
    );
    this.secret = Objects.requireNonNull(secret, "Secret is required");
  }

  public String getSecret() {
    return secret;
  }
}
