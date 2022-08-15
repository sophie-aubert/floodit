package ch.comem.archidep.floodit.games.data;

import ch.comem.archidep.floodit.games.Game;
import org.apache.commons.lang3.builder.Builder;

public class PlayMoveDtoBuilder implements Builder<PlayMoveDto> {

  private Long gameId;
  private Integer color;

  public PlayMoveDtoBuilder(Long gameId, Integer color) {
    this.gameId = gameId;
    this.color = color;
  }

  public PlayMoveDtoBuilder withGame(Game game) {
    return this.withGameId(game.getId());
  }

  public PlayMoveDtoBuilder withGameId(Long gameId) {
    this.gameId = gameId;
    return this;
  }

  public PlayMoveDtoBuilder withColor(Integer color) {
    this.color = color;
    return this;
  }

  @Override
  public PlayMoveDto build() {
    return new PlayMoveDto(this.gameId, this.color);
  }
}
