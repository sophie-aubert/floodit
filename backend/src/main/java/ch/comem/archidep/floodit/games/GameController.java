package ch.comem.archidep.floodit.games;

import ch.comem.archidep.floodit.FloodItRoutes;
import ch.comem.archidep.floodit.games.data.CreateGameDto;
import ch.comem.archidep.floodit.games.data.CreatedGameDto;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(FloodItRoutes.GAMES)
@Validated
public class GameController {

  @Autowired
  private GameService gameService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CreatedGameDto createGame(
    @Valid @RequestBody CreateGameDto requestBody
  ) {
    return this.gameService.createGame(requestBody);
  }
}
