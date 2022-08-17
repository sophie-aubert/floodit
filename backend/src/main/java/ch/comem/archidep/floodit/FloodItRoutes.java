package ch.comem.archidep.floodit;

public final class FloodItRoutes {

  public static final String GAMES = "/api/games";
  public static final String GAME = GAMES + Segments.GAME_ID;
  public static final String GAME_BOARD = GAME + Segments.BOARD;
  public static final String MOVES = "/api/moves";

  public static final class Segments {

    public static final String BOARD = "/board";
    public static final String GAME_ID = "/{gameId:\\d+}";

    private Segments() {}
  }

  private FloodItRoutes() {}
}
