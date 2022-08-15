package ch.comem.archidep.floodit.games;

import ch.comem.archidep.floodit.business.Board;
import ch.comem.archidep.floodit.games.data.CreateGameDto;
import ch.comem.archidep.floodit.games.data.CreatedGameDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SortNatural;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = Game.TABLE_NAME)
public class Game {

  public static final String TABLE_NAME = "games";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String secret;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private GameState state;

  @Column(nullable = false)
  private String playerName;

  @Column(nullable = false)
  private int boardWidth;

  @Column(nullable = false)
  private int boardHeight;

  @Column(nullable = false)
  private int numberOfColors;

  @Column(nullable = false)
  private int maxMoves;

  @Column(nullable = false)
  private long seed;

  @OneToMany(mappedBy = "game")
  @SortNatural
  private List<Move> moves;

  @CreationTimestamp
  @Column(nullable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(nullable = false)
  private LocalDateTime updatedAt;

  @Version
  @Column(nullable = false)
  private Long version;

  Game(CreateGameDto dto) {
    this();
    Objects.requireNonNull(dto, "DTO is required");
    this.secret = RandomStringUtils.randomAscii(255);
    this.state = GameState.ONGOING;
    this.playerName =
      Objects.requireNonNull(dto.playerName(), "Player name is required");
    this.boardWidth = dto.boardWidth();
    this.boardHeight = dto.boardHeight();
    this.numberOfColors = dto.numberOfColors();
    this.maxMoves = dto.maxMoves();
    this.seed =
      Board.generateSeed(
        dto.boardWidth(),
        dto.boardHeight(),
        dto.numberOfColors()
      );
  }

  Game() {
    // No-args constructor for Hibernate
    this.moves = new ArrayList<>();
  }

  public Board buildCurrentBoard() {
    var board = new Board(
      this.boardWidth,
      this.boardHeight,
      this.numberOfColors,
      this.seed
    );

    for (var move : this.moves) {
      board.flood(move.getColor());
    }

    return board;
  }

  public CreatedGameDto toCreatedDto() {
    return new CreatedGameDto(
      this.id,
      this.secret,
      this.state,
      this.playerName,
      this.boardWidth,
      this.boardHeight,
      this.numberOfColors,
      this.maxMoves,
      new ArrayList<>(),
      this.createdAt,
      this.updatedAt
    );
  }

  public void touch() {
    this.updatedAt = LocalDateTime.now();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
      .append("playerName", this.playerName)
      .append(
        "boardSize",
        String.format("%sx%s", this.boardWidth, this.boardHeight)
      )
      .append("numberOfColors", this.numberOfColors)
      .build();
  }

  public Long getId() {
    return id;
  }

  public String getSecret() {
    return secret;
  }

  public GameState getState() {
    return state;
  }

  public void setState(GameState state) {
    this.state = state;
  }

  public String getPlayerName() {
    return playerName;
  }

  public int getBoardWidth() {
    return boardWidth;
  }

  public int getBoardHeight() {
    return boardHeight;
  }

  public int getNumberOfColors() {
    return numberOfColors;
  }

  public int getMaxMoves() {
    return maxMoves;
  }

  public long getSeed() {
    return seed;
  }

  public List<Move> getMoves() {
    return moves;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public Long getVersion() {
    return version;
  }
}
