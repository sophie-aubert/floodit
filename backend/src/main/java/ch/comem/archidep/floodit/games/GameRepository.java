package ch.comem.archidep.floodit.games;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
  List<Game> findTop10ByOrderByCreatedAtDesc();
}
