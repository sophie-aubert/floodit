import { Component, OnInit } from '@angular/core';
import { DateTime } from 'luxon';
import { BehaviorSubject, filter, first, Observable, switchMap } from 'rxjs';
import { colors } from 'src/app/games/game.constants';
import { Game, GameState } from 'src/app/games/game.model';
import { CurrentGame, GameService } from 'src/app/games/game.service';
import { NavigationService } from 'src/app/navigation.service';

enum State {
  Loading,
  Playing,
  Error
}

type Playing = {
  readonly game: Game;
  readonly board: number[][];
  readonly playableColors: string[];
};

@Component({
  selector: 'app-current-game',
  templateUrl: './current-game.component.html',
  styleUrls: ['./current-game.component.css']
})
export class CurrentGameComponent implements OnInit {
  state: State;
  currentGame?: CurrentGame;
  playableColors?: string[];

  readonly palette = colors;
  readonly DateTime = DateTime;
  readonly GameState = GameState;
  readonly State = State;

  constructor(
    private readonly gameService: GameService,
    private readonly navigationService: NavigationService
  ) {
    this.state = State.Loading;
  }

  get game(): Game | undefined {
    return this.currentGame?.game;
  }

  get board(): number[][] | undefined {
    return this.currentGame?.board;
  }

  get progress(): number | undefined {
    if (!this.game) {
      return;
    }

    return Math.round((this.game.moves.length * 100) / this.game.maxMoves);
  }

  ngOnInit(): void {
    this.gameService.loadCurrentGame$().subscribe({
      next: currentGame => {
        if (!currentGame) {
          return this.navigationService.goToDashboard();
        }

        this.state = State.Playing;
        this.currentGame = currentGame;
        this.playableColors = this.palette.slice(
          0,
          currentGame.game.numberOfColors
        );
      },
      error: () => (this.state = State.Error)
    });
  }

  play(color: number): void {
    if (this.state !== State.Playing || !this.currentGame) {
      return;
    }

    this.gameService
      .play$(this.currentGame, color)
      .subscribe(currentGame => (this.currentGame = currentGame));
  }

  playAnotherGame(): void {
    this.gameService.stopCurrentGame();
  }
}
