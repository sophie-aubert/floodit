package ch.comem.archidep.floodit.games.exceptions;

import ch.comem.archidep.floodit.errors.FloodItException;
import ch.comem.archidep.floodit.games.Game;

public class GameForbidden extends FloodItException {

  public GameForbidden(Game game) {
    super(
      String.format(
        "Game %s cannot be modified without the correct credentials",
        game.getId()
      )
    );
  }
}
