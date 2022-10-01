import { Injectable } from '@angular/core';
import { forkJoin, map, Observable, of, switchMap } from 'rxjs';
import { NavigationService } from '../navigation.service';
import { GameApiService } from './game-api.service';
import { CreatedGame, CreateGameParams, Game } from './game.model';

const storageKey = 'floodit.game';

type LocalGameData = {
  readonly currentGame: {
    readonly id: number;
    readonly secret: string;
  };
};

export type CurrentGame = {
  readonly game: CreatedGame;
  readonly board: number[][];
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
            board
          }))
        );
      })
    );
  }

  play$(currentGame: CurrentGame, color: number): Observable<CurrentGame> {
    return this.gameApi
      .play$({
        gameId: currentGame.game.id,
        color,
        secret: currentGame.game.secret
      })
      .pipe(
        map(move => {
          const newGame: CreatedGame = {
            ...currentGame.game,
            state: move.gameState,
            moves: [...currentGame.game.moves, move]
          };

          const newBoard = currentGame.board.slice().map(row => row.slice());

          for (const currentMove of newGame.moves) {
            for (const [col, row] of currentMove.flooded) {
              newBoard[row][col] = color;
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
        game: { ...game, secret: currentGame.secret },
        board
      }))
    );
  }

  stopCurrentGame(): void {
    this.#deleteLocalGameData();
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

  #deleteLocalGameData(): void {
    localStorage.removeItem(storageKey);
    this.navigationService.goToDashboard();
  }
}
