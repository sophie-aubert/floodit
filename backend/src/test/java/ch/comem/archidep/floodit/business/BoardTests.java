package ch.comem.archidep.floodit.business;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

import org.junit.jupiter.api.Test;

class BoardTests {

  @Test
  void flood_a_3x3_board_in_2_moves() {
    // 2 2 2
    // 0 0 2
    // 0 1 1
    var board = new Board(3, 3, 3, 42);

    assertThat(board.isOneColor(), is(false));

    // 0 0 0
    // 0 0 0
    // 0 1 1
    assertThat(
      board.flood(0),
      containsInAnyOrder(
        Position.at(0, 0),
        Position.at(1, 0),
        Position.at(2, 0),
        Position.at(2, 1)
      )
    );

    assertThat(board.isOneColor(), is(false));

    // 1 1 1
    // 1 1 1
    // 1 1 1
    assertThat(
      board.flood(1),
      containsInAnyOrder(
        Position.at(0, 1),
        Position.at(0, 2),
        Position.at(1, 1)
      )
    );

    assertThat(board.isOneColor(), is(true));
  }

  @Test
  void flood_a_5x5_board_in_6_moves() {
    // 2 1 3 1 1
    // 2 1 0 1 2
    // 3 3 3 3 1
    // 0 2 0 3 0
    // 0 1 1 2 3
    var board = new Board(5, 5, 4, 24);

    assertThat(board.isOneColor(), is(false));

    // 3 1 3 1 1
    // 3 1 0 1 2
    // 3 3 3 3 1
    // 0 2 0 3 0
    // 0 1 1 2 3
    assertThat(
      board.flood(3),
      containsInAnyOrder(Position.at(0, 0), Position.at(0, 1))
    );

    assertThat(board.isOneColor(), is(false));

    // 1 1 3 1 1
    // 1 1 0 1 2
    // 1 1 1 1 1
    // 0 2 0 1 0
    // 0 1 1 2 3
    assertThat(
      board.flood(1),
      containsInAnyOrder(
        Position.at(0, 2),
        Position.at(1, 2),
        Position.at(2, 2),
        Position.at(3, 2),
        Position.at(3, 3)
      )
    );

    assertThat(board.isOneColor(), is(false));

    // 0 0 3 0 0
    // 0 0 0 0 2
    // 0 0 0 0 0
    // 0 2 0 0 0
    // 0 1 1 2 3
    assertThat(
      board.flood(0),
      containsInAnyOrder(
        Position.at(1, 0),
        Position.at(1, 1),
        Position.at(3, 0),
        Position.at(3, 1),
        Position.at(4, 0),
        Position.at(4, 2)
      )
    );

    assertThat(board.isOneColor(), is(false));

    // 2 2 3 2 2
    // 2 2 2 2 2
    // 2 2 2 2 2
    // 2 2 2 2 2
    // 2 1 1 2 3
    assertThat(
      board.flood(2),
      containsInAnyOrder(
        Position.at(0, 3),
        Position.at(0, 4),
        Position.at(2, 1),
        Position.at(2, 3),
        Position.at(4, 3)
      )
    );

    assertThat(board.isOneColor(), is(false));

    // 1 1 3 1 1
    // 1 1 1 1 1
    // 1 1 1 1 1
    // 1 1 1 1 1
    // 1 1 1 1 3
    assertThat(
      board.flood(1),
      containsInAnyOrder(
        Position.at(1, 3),
        Position.at(3, 4),
        Position.at(4, 1)
      )
    );

    assertThat(board.isOneColor(), is(false));

    // 3 3 3 3 3
    // 3 3 3 3 3
    // 3 3 3 3 3
    // 3 3 3 3 3
    // 3 3 3 3 3
    assertThat(
      board.flood(3),
      containsInAnyOrder(Position.at(1, 4), Position.at(2, 4))
    );

    assertThat(board.isOneColor(), is(true));
  }
}
