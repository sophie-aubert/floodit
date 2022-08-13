package ch.comem.archidep.floodit.games;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GameService {

  @Autowired
  private GameRepository gameRepository;

  public Game createGame() {
    var game = new Game();

    return this.gameRepository.saveAndFlush(game);
  }
}
