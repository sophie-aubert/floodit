import { DateTime } from 'luxon';

export interface CreateGameParams {
  readonly playerName: string;
  readonly boardWidth: number;
  readonly boardHeight: number;
  readonly numberOfColors: number;
  readonly maxMoves: number;
}

export interface Game extends CreateGameParams {
  readonly id: number;
  readonly createdAt: DateTime;
  readonly updatedAt: DateTime;
}

export interface CreatedGame extends Game {
  readonly secret: string;
}
