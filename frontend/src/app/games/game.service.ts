import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, map, Observable, Subject, tap } from 'rxjs';
import { RemoteData } from '../utils/data';
import { GameApiService } from './game-api.service';
import { CreateGameParams, Game } from './game.model';

const storageKey = 'floodit.game';

type LocalGameData = {
  readonly currentGame: {
    readonly id: number;
    readonly secret: string;
  };
};

@Injectable({
  providedIn: 'root'
})
export class GameService {
  readonly currentGame$: Observable<RemoteData<Game>>;
  readonly games$: Observable<RemoteData<Game[]>>;

  #currentGame$: Subject<RemoteData<Game>>;
  #games$: Subject<RemoteData<Game[]>>;

  constructor(
    private readonly gameApi: GameApiService,
    private readonly router: Router
  ) {
    this.#currentGame$ = new BehaviorSubject<RemoteData<Game>>({
      state: 'notLoaded'
    });
    this.currentGame$ = this.#currentGame$.asObservable();

    this.#games$ = new BehaviorSubject<RemoteData<Game[]>>({
      state: 'notLoaded'
    });
    this.games$ = this.#games$.asObservable();

    this.#loadCurrentGameIfAny();
  }

  loadGames(): void {
    this.#games$.next({ state: 'loading' });
    this.gameApi.listGames$().subscribe({
      next: data => this.#games$.next({ state: 'loaded', data }),
      error: error => this.#games$.next({ state: 'error', error })
    });
  }

  startGame$(params: CreateGameParams): Observable<void> {
    return this.gameApi.createGame$(params).pipe(
      tap(createdGame => {
        this.#currentGame$.next({ state: 'loaded', data: createdGame });
        this.#saveLocalGameData({
          currentGame: { id: createdGame.id, secret: createdGame.secret }
        });
        this.#goToCurrentGame();
      }),
      map(() => undefined)
    );
  }

  #loadCurrentGameIfAny() {
    const localGameData = this.#loadLocalGameData();

    const currentGame = localGameData?.currentGame;
    if (currentGame !== undefined) {
      this.#currentGame$.next({ state: 'loading' });
      this.gameApi.loadGame$(currentGame.id).subscribe({
        next: data => {
          this.#currentGame$.next({ state: 'loaded', data });
          this.#goToCurrentGame();
        },
        error: error => this.#currentGame$.next({ state: 'error', error })
      });
    }
  }

  #goToCurrentGame() {
    this.router.navigate(['/game']);
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
