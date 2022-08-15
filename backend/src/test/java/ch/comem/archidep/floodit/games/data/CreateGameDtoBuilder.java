package ch.comem.archidep.floodit.games.data;

import org.apache.commons.lang3.builder.Builder;

public class CreateGameDtoBuilder implements Builder<CreateGameDto> {

  private String playerName;
  private Integer boardWidth;
  private Integer boardHeight;
  private Integer numberOfColors;
  private Integer maxMoves;

  public CreateGameDtoBuilder(
    String playerName,
    Integer boardWidth,
    Integer boardHeight,
    Integer numberOfColors,
    Integer maxMoves
  ) {
    this.playerName = playerName;
    this.boardWidth = boardWidth;
    this.boardHeight = boardHeight;
    this.numberOfColors = numberOfColors;
    this.maxMoves = maxMoves;
  }

  public CreateGameDtoBuilder withPlayerName(String playerName) {
    this.playerName = playerName;
    return this;
  }

  public CreateGameDtoBuilder withBoardWidth(Integer boardWidth) {
    this.boardWidth = boardWidth;
    return this;
  }

  public CreateGameDtoBuilder withBoardHeight(Integer boardHeight) {
    this.boardHeight = boardHeight;
    return this;
  }

  public CreateGameDtoBuilder withNumberOfColors(Integer numberOfColors) {
    this.numberOfColors = numberOfColors;
    return this;
  }

  public CreateGameDtoBuilder withMaxMoves(Integer maxMoves) {
    this.maxMoves = maxMoves;
    return this;
  }

  @Override
  public CreateGameDto build() {
    return new CreateGameDto(
      this.playerName,
      this.boardWidth,
      this.boardHeight,
      this.numberOfColors,
      this.maxMoves
    );
  }
}
