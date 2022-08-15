package ch.comem.archidep.floodit.games.data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record CreateGameDto(
  @NotBlank String playerName,
  @NotNull @Min(2) @Max(100) Integer boardWidth,
  @NotNull @Min(2) @Max(100) Integer boardHeight,
  @NotNull @Min(2) @Max(10) Integer numberOfColors,
  @NotNull @Min(1) Integer maxMoves
) {}
