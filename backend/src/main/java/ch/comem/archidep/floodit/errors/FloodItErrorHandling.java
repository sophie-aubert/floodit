package ch.comem.archidep.floodit.errors;

import ch.comem.archidep.floodit.games.exceptions.GameForbidden;
import ch.comem.archidep.floodit.games.exceptions.GameNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FloodItErrorHandling {

  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ExceptionHandler(GameForbidden.class)
  public void handleGameForbidden() {
    // Nothing to return
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(GameNotFound.class)
  public void handleGameNotFound() {
    // Nothing to return
  }
}
