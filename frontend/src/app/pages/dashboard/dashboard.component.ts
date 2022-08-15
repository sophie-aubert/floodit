import { Component, OnInit } from '@angular/core';
import { DateTime } from 'luxon';
import { GameApiService } from 'src/app/games/game-api.service';
import { Game } from 'src/app/games/game.model';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  games?: Game[];

  readonly DateTime = DateTime;

  constructor(private gameApi: GameApiService) {}

  ngOnInit(): void {
    this.gameApi.listGames$().subscribe({
      next: games => (this.games = games)
    });
  }

  createGame(): void {
    this.gameApi
      .createGame$({
        playerName: 'John Doe',
        boardWidth: 10,
        boardHeight: 10,
        numberOfColors: 4,
        maxMoves: 10
      })
      .subscribe({
        next: createdGame => console.log('@@@', createdGame)
      });
  }
}
