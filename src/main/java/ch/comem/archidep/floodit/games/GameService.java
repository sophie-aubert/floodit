package ch.comem.archidep.floodit.games;

import org.springframework.stereotype.Service;

@Service
public class GameService {
  public Game createGame() {
    return new Game();
  }
}
