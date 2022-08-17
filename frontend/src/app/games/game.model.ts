export interface CreateGameParams {
  readonly playerName: string;
  readonly boardWidth: number;
  readonly boardHeight: number;
  readonly numberOfColors: number;
  readonly maxMoves: number;
}

export enum GameState {
  Ongoing = 'ONGOING',
  Win = 'WIN',
  Loss = 'LOSS'
}

export interface Game extends CreateGameParams {
  readonly id: number;
  readonly state: GameState;
  readonly playerName: string;
  readonly boardWidth: number;
  readonly boardHeight: number;
  readonly numberOfColors: number;
  readonly maxMoves: number;
  readonly moves: Move[];
  readonly createdAt: string;
  readonly updatedAt: string;
}

export interface Move {
  readonly id: number;
  readonly color: number;
  readonly flooded: number[];
  readonly position: [number, number];
  readonly gameState: GameState;
  readonly createdAt: string;
}

export interface CreatedGame extends Game {
  readonly secret: string;
}

export class Board {
  readonly positions: ReadonlyArray<ReadonlyArray<number>>;

  constructor(game: Game) {
    this.positions = Array(game.boardHeight)
      .fill(0)
      .map((_rowValue, row) =>
        Array(game.boardWidth)
          .fill(0)
          .map((_columnValue, column) => column * row)
      );
  }
}
