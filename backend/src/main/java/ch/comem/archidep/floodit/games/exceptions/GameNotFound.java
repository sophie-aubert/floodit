package ch.comem.archidep.floodit.games.exceptions;

import ch.comem.archidep.floodit.errors.FloodItException;

public class GameNotFound extends FloodItException {

  public GameNotFound(Long id) {
    super(
      String.format("Game with ID %s could not be found in the database", id)
    );
  }
}
