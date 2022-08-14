package ch.comem.archidep.floodit.games;

import ch.comem.archidep.floodit.games.data.CreateGameDto;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = Game.TABLE_NAME)
public class Game {

  public static final String TABLE_NAME = "games";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String playerName;

  @Column(nullable = false)
  private int boardWidth;

  @Column(nullable = false)
  private int boardHeight;

  @Column(nullable = false)
  private int numberOfColors;

  @Column(nullable = false)
  private long seed;

  @CreationTimestamp
  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Version
  @Column(nullable = false)
  private Long version;

  Game(CreateGameDto dto) {
    Objects.requireNonNull(dto, "DTO is required");
    this.setPlayerName(dto.playerName());
    this.setBoardWidth(dto.boardWidth());
    this.setBoardHeight(dto.boardHeight());
    this.setNumberOfColors(dto.numberOfColors());
    this.setSeed(new Random().nextLong());
  }

  Game() {
    // Nothing to do
  }

  public Long getId() {
    return id;
  }

  public String getPlayerName() {
    return playerName;
  }

  public void setPlayerName(String playerName) {
    this.playerName =
      Objects.requireNonNull(playerName, "Player name is required");
  }

  public int getBoardWidth() {
    return boardWidth;
  }

  public void setBoardWidth(int boardWidth) {
    this.boardWidth = boardWidth;
  }

  public int getBoardHeight() {
    return boardHeight;
  }

  public void setBoardHeight(int boardHeight) {
    this.boardHeight = boardHeight;
  }

  public int getNumberOfColors() {
    return numberOfColors;
  }

  public void setNumberOfColors(int numberOfColors) {
    this.numberOfColors = numberOfColors;
  }

  public long getSeed() {
    return seed;
  }

  public void setSeed(long seed) {
    this.seed = seed;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public Long getVersion() {
    return version;
  }
}
