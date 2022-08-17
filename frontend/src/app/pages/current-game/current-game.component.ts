import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DateTime } from 'luxon';
import { Board, Game } from 'src/app/games/game.model';

@Component({
  selector: 'app-current-game',
  templateUrl: './current-game.component.html',
  styleUrls: ['./current-game.component.scss']
})
export class CurrentGameComponent {
  readonly game: Game;
  readonly board: Board;

  readonly DateTime = DateTime;

  constructor(route: ActivatedRoute) {
    this.game = route.snapshot.data['currentGame'];
    this.board = new Board(this.game);
  }
}
