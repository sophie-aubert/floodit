package ch.comem.archidep.floodit.rules;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Board {

  private static final Random RANDOM = new Random();

  public static long generateSeed(int width, int height, int numberOfColors) {
    while (true) {
      var seed = RANDOM.nextLong();
      var board = new Board(width, height, numberOfColors, seed);
      if (!board.isOneColor()) {
        return seed;
      }
    }
  }

  private final int[][] colors;
  private final int numberOfColors;
  private final Set<Position> flooded;

  public Board(int width, int height, int numberOfColors, long seed) {
    if (width < 2 || width > 100) {
      throw new IllegalArgumentException(
        String.format(
          "Width must be between 2 and 100 but its value is %s",
          width
        )
      );
    } else if (height < 2 || height > 100) {
      throw new IllegalArgumentException(
        String.format(
          "Height must be between 2 and 100 but its value is %s",
          width
        )
      );
    } else if (numberOfColors < 2 || numberOfColors > 10) {
      throw new IllegalArgumentException(
        String.format(
          "Number of colors must be between 2 and 10 but its value is %s",
          width
        )
      );
    }

    this.colors = new int[width][height];
    this.numberOfColors = numberOfColors;
    this.flooded = new HashSet<>();

    var random = new Random(seed);
    for (var column = 0; column < width; column++) {
      for (var row = 0; row < height; row++) {
        this.colors[column][row] = random.nextInt(numberOfColors);
      }
    }
  }

  public int getWidth() {
    return this.colors.length;
  }

  public int getHeight() {
    return this.colors[0].length;
  }

  public int getColor(Position position) {
    if (
      position.getColumn() >= this.getWidth() ||
      position.getRow() >= this.getHeight()
    ) {
      throw new IllegalArgumentException(
        String.format("Position %s is outside board %s", position, this)
      );
    }

    return this.colors[position.getColumn()][position.getRow()];
  }

  public boolean isOneColor() {
    var color = this.colors[0][0];

    for (var column = 0; column < this.colors.length; column++) {
      for (var row = 0; row < this.colors.length; row++) {
        if (this.colors[column][row] != color) {
          return false;
        }
      }
    }

    return true;
  }

  public Set<Position> flood(int newColor) {
    if (newColor < 0 || newColor >= this.numberOfColors) {
      throw new IllegalArgumentException(
        String.format(
          "Unknown color %s (valid colors are 0-%s)",
          newColor,
          this.numberOfColors - 1
        )
      );
    }

    var currentColor = this.colors[0][0];
    var newFlood = new HashSet<Position>();
    this.findAdjacentPositionsWithTheSameColor(
        newFlood,
        new Position(0, 0),
        currentColor
      );

    newFlood.forEach(position ->
      this.colors[position.getColumn()][position.getRow()] = newColor
    );

    newFlood.removeAll(this.flooded);
    this.flooded.addAll(newFlood);

    return newFlood;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
      .append("width", this.getWidth())
      .append("height", this.getHeight())
      .append("numberOfColors", this.numberOfColors)
      .build();
  }

  private void findAdjacentPositionsWithTheSameColor(
    Set<Position> accumulator,
    Position position,
    int color
  ) {
    if (accumulator.contains(position) || this.getColor(position) != color) {
      return;
    }

    accumulator.add(position);

    position
      .getAdjacentPositions()
      .stream()
      .filter(this::isOnBoard)
      .forEach(newPosition ->
        this.findAdjacentPositionsWithTheSameColor(
            accumulator,
            newPosition,
            color
          )
      );
  }

  private boolean isOnBoard(Position position) {
    return (
      position.getColumn() < this.getWidth() &&
      position.getRow() < this.getHeight()
    );
  }
}
