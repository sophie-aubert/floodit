package ch.comem.archidep.floodit.games;

import ch.comem.archidep.floodit.errors.FloodItBusinessException;
import java.io.Serializable;
import java.util.Map;

public class GameException extends FloodItBusinessException {

  GameException(GameErrorCode code, Map<String, Serializable> data) {
    super(code.name(), data);
  }
}
