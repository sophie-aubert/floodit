import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CreatedGame, CreateGameParams, Game } from './game.model';

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
}
