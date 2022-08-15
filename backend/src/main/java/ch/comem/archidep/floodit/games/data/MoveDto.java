package ch.comem.archidep.floodit.games.data;

import ch.comem.archidep.floodit.business.Position;
import ch.comem.archidep.floodit.games.GameState;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

public record MoveDto(
  long id,
  int color,
  Set<Position> flooded,
  GameState gameState,
  LocalDateTime createdAt
) {
  public MoveDto {
    Objects.requireNonNull(flooded, "Flooded positions are required");
    Objects.requireNonNull(gameState, "Game state is required");
    Objects.requireNonNull(createdAt, "Creation date is required");
  }
}
