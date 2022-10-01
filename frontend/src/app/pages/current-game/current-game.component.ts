import { Component, OnInit } from '@angular/core';
import { DateTime } from 'luxon';
import { BehaviorSubject, filter, first, Observable, switchMap } from 'rxjs';
import { colors } from 'src/app/games/game.constants';
import { Game, GameState } from 'src/app/games/game.model';
import { CurrentGame, GameService } from 'src/app/games/game.service';
import { NavigationService } from 'src/app/navigation.service';

enum CurrentGameState {
  GameIsLoading,
  Playing
}

type GameIsLoading = {
  readonly name: CurrentGameState.GameIsLoading;
};

type Playing = {
  readonly name: CurrentGameState.Playing;
  readonly game: Game;
  readonly board: number[][];
  readonly playableColors: string[];
};

type State = GameIsLoading | Playing;

@Component({
  selector: 'app-current-game',
  templateUrl: './current-game.component.html',
  styleUrls: ['./current-game.component.css']
})
export class CurrentGameComponent implements OnInit {
  readonly state$: Observable<State>;
  readonly CurrentGameState = CurrentGameState;
  readonly DateTime = DateTime;
  readonly GameState = GameState;

  readonly #state$: BehaviorSubject<State>;

  constructor(
    private readonly gameService: GameService,
    private readonly navigationService: NavigationService
  ) {
    this.#state$ = new BehaviorSubject<State>({
      name: CurrentGameState.GameIsLoading
    });
    this.state$ = this.#state$.asObservable();
  }

  ngOnInit(): void {
    this.gameService.loadCurrentGame$().subscribe(currentGame => {
      if (!currentGame) {
        return this.navigationService.goToDashboard();
      }

      this.#setCurrentGame(currentGame);
    });
  }

  play(color: number): void {
    this.state$
      .pipe(
        first(),
        filter(isPlaying),
        switchMap(({ game, board }) =>
          this.gameService.play$({ game, board }, color)
        )
      )
      .subscribe(currentGame => this.#setCurrentGame(currentGame));
  }

  playAnotherGame(): void {
    this.gameService.stopCurrentGame();
  }

  #setCurrentGame(currentGame: CurrentGame) {
    this.#state$.next({
      name: CurrentGameState.Playing,
      game: currentGame.game,
      board: currentGame.board,
      playableColors: Array(currentGame.game.numberOfColors)
        .fill(0)
        .map((_value, i) => colors[i])
    });
  }
}

function isPlaying(state: State): state is Playing {
  return state.name === CurrentGameState.Playing;
}
