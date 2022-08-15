package ch.comem.archidep.floodit.games.data;

import ch.comem.archidep.floodit.games.GameState;
import ch.comem.archidep.floodit.games.Move;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public record CreatedGameDto(
  long id,
  String secret,
  GameState state,
  String playerName,
  int boardWidth,
  int boardHeight,
  int numberOfColors,
  int maxMoves,
  List<Move> moves,
  LocalDateTime createdAt,
  LocalDateTime updatedAt
) {
  public CreatedGameDto {
    Objects.requireNonNull(secret, "Secret is required");
    Objects.requireNonNull(state, "State is required");
    Objects.requireNonNull(playerName, "Player name is required");
    Objects.requireNonNull(moves, "Moves are required");
    Objects.requireNonNull(createdAt, "Creation date is required");
    Objects.requireNonNull(updatedAt, "Last modification date is required");
  }
}
