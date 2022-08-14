package ch.comem.archidep.floodit.games;

import ch.comem.archidep.floodit.games.data.CreateGameDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GameService {

  @Autowired
  private GameRepository gameRepository;

  public Game createGame(CreateGameDto dto) {
    var game = new Game(dto);

    return this.gameRepository.saveAndFlush(game);
  }
}
