package ch.comem.archidep.floodit;

public final class FloodItRoutes {

  public static final String GAMES = "/api/games";
  public static final String GAME = GAMES + Params.GAME_ID;
  public static final String MOVES = "/api/moves";

  public static final class Params {

    public static final String GAME_ID = "/{gameId:\\d+}";

    private Params() {}
  }

  private FloodItRoutes() {}
}
