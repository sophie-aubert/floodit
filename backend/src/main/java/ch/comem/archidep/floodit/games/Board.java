package ch.comem.archidep.floodit.games;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Board {

  private final int[][] colors;
  private final int numberOfColors;

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

  public Position at(int column, int row) {
    return new Position(column, row);
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
    var flooded = new HashSet<Position>();
    this.findAdjacentPositionsWithTheSameColor(
        flooded,
        new Position(0, 0),
        currentColor
      );

    flooded.forEach(position ->
      this.colors[position.column][position.row] = newColor
    );

    return flooded;
  }

  private int getColor(Position position) {
    return this.colors[position.column][position.row];
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
      .around()
      .stream()
      .forEach(newPosition -> {
        this.findAdjacentPositionsWithTheSameColor(
            accumulator,
            newPosition,
            color
          );
      });
  }

  public class Position {

    private final int column;
    private final int row;

    public Position(int column, int row) {
      if (column < 0 || column >= Board.this.getWidth()) {
        throw new IllegalArgumentException(
          String.format(
            "Column must be an integer between 0 and %s",
            Board.this.getWidth()
          )
        );
      } else if (row < 0 || row >= Board.this.getHeight()) {
        throw new IllegalArgumentException(
          String.format(
            "Row must be an integer between 0 and %s",
            Board.this.getHeight()
          )
        );
      }

      this.column = column;
      this.row = row;
    }

    public Set<Position> around() {
      return Stream
        .of(
          new int[] { this.column + 1, this.row },
          new int[] { this.column + 1, this.row + 1 },
          new int[] { this.column, this.row - 1 },
          new int[] { this.column - 1, this.row - 1 }
        )
        .filter(delta -> {
          var newColumn = delta[0];
          var newRow = delta[1];
          return (
            newColumn >= 0 &&
            newColumn < Board.this.getWidth() &&
            newRow >= 0 &&
            newRow < Board.this.getHeight()
          );
        })
        .map(delta -> new Position(delta[0], delta[1]))
        .collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == this) {
        return true;
      } else if (obj == null || obj.getClass() != getClass()) {
        return false;
      }

      var otherPosition = (Position) obj;
      return new EqualsBuilder()
        .append(this.column, otherPosition.column)
        .append(this.row, otherPosition.row)
        .isEquals();
    }

    @Override
    public int hashCode() {
      return new HashCodeBuilder(17931, 311)
        .append(this.column)
        .append(this.row)
        .toHashCode();
    }

    @Override
    public String toString() {
      return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("column", this.column)
        .append("row", this.row)
        .build();
    }
  }
}
