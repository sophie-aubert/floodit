package ch.comem.archidep.floodit.games;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

import org.junit.jupiter.api.Test;

class BoardTests {

  @Test
  void flood_a_9x9_board() {
    // 2 2 2
    // 0 0 2
    // 0 1 1
    var board = new Board(3, 3, 3, 42);

    // 0 0 0
    // 0 0 0
    // 0 1 1
    assertThat(
      board.flood(0),
      containsInAnyOrder(
        board.at(0, 0),
        board.at(1, 0),
        board.at(2, 0),
        board.at(2, 1)
      )
    );

    // 1 1 1
    // 1 1 1
    // 1 1 1
    assertThat(
      board.flood(1),
      containsInAnyOrder(
        board.at(0, 0),
        board.at(0, 1),
        board.at(0, 2),
        board.at(1, 0),
        board.at(1, 1),
        board.at(2, 0),
        board.at(2, 1)
      )
    );
  }
}
