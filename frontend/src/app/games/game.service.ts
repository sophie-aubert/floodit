import { Injectable } from '@angular/core';
import { forkJoin, map, Observable, of, switchMap } from 'rxjs';
import { NavigationService } from '../navigation.service';
import { GameApiService } from './game-api.service';
import { colors } from './game.constants';
import { CreateGameParams, Game, Move, PlayParams } from './game.model';

const storageKey = 'floodit.game';

type LocalGameData = {
  readonly currentGame: {
    readonly id: number;
    readonly secret: string;
  };
};

export type CurrentGame = {
  readonly game: Game;
  readonly board: string[][];
};

@Injectable({
  providedIn: 'root'
})
export class GameService {
  constructor(
    private readonly gameApi: GameApiService,
    private readonly navigationService: NavigationService
  ) {}

  continueCurrentGame(): boolean {
    const localGameData = this.#loadLocalGameData();
    if (localGameData === undefined) {
      return false;
    }

    this.navigationService.goToCurrentGame();
    return true;
  }

  startGame$(params: CreateGameParams): Observable<CurrentGame> {
    return this.gameApi.createGame$(params).pipe(
      switchMap(createdGame => {
        this.#saveLocalGameData({
          currentGame: { id: createdGame.id, secret: createdGame.secret }
        });

        return this.gameApi.loadGameBoard$(createdGame.id).pipe(
          map(board => ({
            game: createdGame,
            board: board.map(row => row.map(color => colors[color]))
          }))
        );
      })
    );
  }

  play$(currentGame: CurrentGame, color: number): Observable<CurrentGame> {
    return this.gameApi.play$({ gameId: currentGame.game.id, color }).pipe(
      map(move => {
        const newGame: Game = {
          ...currentGame.game,
          moves: [...currentGame.game.moves, move]
        };

        const newBoard = currentGame.board.slice().map(row => row.slice());

        for (const currentMove of newGame.moves) {
          for (const [col, row] of currentMove.flooded) {
            newBoard[row][col] = colors[color];
          }
        }

        return {
          game: newGame,
          board: newBoard
        };
      })
    );
  }

  loadCurrentGame$(): Observable<CurrentGame | undefined> {
    const localGameData = this.#loadLocalGameData();

    const currentGame = localGameData?.currentGame;
    if (currentGame === undefined) {
      return of(undefined);
    }

    return forkJoin([
      this.gameApi.loadGame$(currentGame.id),
      this.gameApi.loadGameBoard$(currentGame.id)
    ]).pipe(
      map(([game, board]) => ({
        game,
        board: board.map(row => row.map(color => colors[color]))
      }))
    );
  }

  #saveLocalGameData(data: LocalGameData): void {
    localStorage.setItem(storageKey, JSON.stringify(data));
  }

  #loadLocalGameData(): LocalGameData | undefined {
    const storedData = localStorage.getItem(storageKey);
    return storedData === null
      ? undefined
      : (JSON.parse(storedData) as LocalGameData);
  }
}
