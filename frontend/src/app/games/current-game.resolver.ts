import { Injectable } from '@angular/core';
import { Resolve, Router } from '@angular/router';
import { first, Observable, of, switchMap, throwError } from 'rxjs';
import { Game } from './game.model';
import { GameService } from './game.service';

@Injectable({ providedIn: 'root' })
export class CurrentGameResolver implements Resolve<Game> {
  constructor(
    private readonly gameService: GameService,
    private readonly router: Router
  ) {}

  resolve(): Observable<Game> {
    return this.gameService.currentGame$.pipe(
      first(),
      switchMap(remoteData => {
        if (remoteData.state === 'loaded') {
          return of(remoteData.data);
        }

        this.router.navigate(['/']);
        return throwError(() => new Error('Current game not loaded'));
      })
    );
  }
}
