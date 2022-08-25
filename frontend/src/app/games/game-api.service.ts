import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {
  CreatedGame,
  CreateGameParams,
  Game,
  Move,
  PlayParams
} from './game.model';

@Injectable({
  providedIn: 'root'
})
export class GameApiService {
  constructor(private readonly http: HttpClient) {}

  createGame$(params: CreateGameParams): Observable<CreatedGame> {
    return this.http.post<CreatedGame>('/api/games', params);
  }

  listGames$(): Observable<Game[]> {
    return this.http.get<Game[]>('/api/games');
  }

  loadGame$(id: number): Observable<Game> {
    return this.http.get<Game>(`/api/games/${id}`);
  }

  loadGameBoard$(id: number): Observable<number[][]> {
    return this.http.get<Array<Array<number>>>(`/api/games/${id}/board`);
  }

  play$({ gameId, color }: PlayParams): Observable<Move> {
    return this.http.post<Move>('/api/moves', { gameId, color });
  }
}
