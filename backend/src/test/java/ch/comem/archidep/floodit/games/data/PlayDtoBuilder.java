package ch.comem.archidep.floodit.games.data;

import ch.comem.archidep.floodit.games.Game;
import org.apache.commons.lang3.builder.Builder;

public class PlayDtoBuilder implements Builder<PlayDto> {

  private Long gameId;
  private Integer color;

  public PlayDtoBuilder(Long gameId, Integer color) {
    this.gameId = gameId;
    this.color = color;
  }

  public PlayDtoBuilder withGame(Game game) {
    return this.withGameId(game.getId());
  }

  public PlayDtoBuilder withGameId(Long gameId) {
    this.gameId = gameId;
    return this;
  }

  public PlayDtoBuilder withColor(Integer color) {
    this.color = color;
    return this;
  }

  @Override
  public PlayDto build() {
    return new PlayDto(this.gameId, this.color);
  }
}
