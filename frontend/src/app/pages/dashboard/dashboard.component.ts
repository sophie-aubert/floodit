import { Component, OnInit } from '@angular/core';
import { DateTime } from 'luxon';
import { GameService } from 'src/app/games/game.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  readonly DateTime = DateTime;

  constructor(readonly gameService: GameService) {}

  ngOnInit(): void {
    this.gameService.loadGames();
  }

  createGame(): void {
    this.gameService
      .startGame$({
        playerName: 'John Doe',
        boardWidth: 10,
        boardHeight: 10,
        numberOfColors: 4,
        maxMoves: 10
      })
      .subscribe();
  }
}
