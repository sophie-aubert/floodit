import { Component, OnInit } from '@angular/core';
import { DateTime } from 'luxon';
import { BehaviorSubject, map, Observable } from 'rxjs';
import { GameApiService } from 'src/app/games/game-api.service';
import { Game } from 'src/app/games/game.model';
import { GameService } from 'src/app/games/game.service';
import { NavigationService } from 'src/app/navigation.service';
import {
  loaded,
  loading,
  RemoteData
} from 'src/app/utils/remote-data/remote-data.model';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  readonly games$: Observable<RemoteData<Game[]>>;
  readonly DateTime = DateTime;

  readonly #games$: BehaviorSubject<RemoteData<Game[]>>;

  constructor(
    private readonly gameApiService: GameApiService,
    private readonly gameService: GameService,
    private readonly navigationService: NavigationService
  ) {
    this.#games$ = new BehaviorSubject<RemoteData<Game[]>>(loading());
    this.games$ = this.#games$.asObservable();
  }

  ngOnInit(): void {
    this.gameApiService.listGames$().pipe(map(loaded)).subscribe(this.#games$);
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
      .subscribe(() => this.navigationService.goToCurrentGame());
  }

  asGames(games: any): Game[] {
    return games;
  }
}
