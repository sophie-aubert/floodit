package ch.comem.archidep.floodit.utils;

import ch.comem.archidep.floodit.games.GameRepository;
import ch.comem.archidep.floodit.games.MoveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DatabaseTestUtils {

  @Autowired
  private GameRepository gameRepository;

  @Autowired
  private MoveRepository moveRepository;

  @Transactional
  public void resetDatabase() {
    this.moveRepository.deleteAll();
    this.gameRepository.deleteAll();
  }
}
