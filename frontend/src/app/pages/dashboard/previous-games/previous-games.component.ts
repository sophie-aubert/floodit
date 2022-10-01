import { Component, OnInit } from '@angular/core';
import { DateTime } from 'luxon';
import { GameApiService } from 'src/app/games/game-api.service';
import { Game, GameState } from 'src/app/games/game.model';

enum State {
  Loading,
  Loaded,
  Error
}

@Component({
  selector: 'app-previous-games',
  templateUrl: './previous-games.component.html',
  styleUrls: ['./previous-games.component.css']
})
export class PreviousGamesComponent implements OnInit {
  readonly games: Game[];
  state: State;

  readonly DateTime = DateTime;
  readonly GameState = GameState;

  constructor(private readonly gameApiService: GameApiService) {
    this.games = [];
    this.state = State.Loading;
  }

  ngOnInit(): void {
    this.gameApiService.listGames$().subscribe({
      next: games => {
        this.games.push(...games);
        this.state = State.Loaded;
      },
      error: () => (this.state = State.Error)
    });
  }
}
