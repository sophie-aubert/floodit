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
  readonly moves: ReadonlyArray<Move>;
  readonly createdAt: string;
  readonly updatedAt: string;
}

export interface CreatedGame extends Game {
  readonly secret: string;
}

export interface Move {
  readonly id: number;
  readonly color: number;
  readonly flooded: [number, number][];
  readonly position: [number, number];
  readonly gameState: GameState;
  readonly createdAt: string;
}

export type PlayParams = {
  readonly gameId: number;
  readonly color: number;
};
