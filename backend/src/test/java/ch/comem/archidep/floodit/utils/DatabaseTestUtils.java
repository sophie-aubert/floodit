package ch.comem.archidep.floodit.utils;

import ch.comem.archidep.floodit.games.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DatabaseTestUtils {

  @Autowired
  private GameRepository gameRepository;

  @Transactional
  public void resetDatabase() {
    this.gameRepository.deleteAll();
  }
}
