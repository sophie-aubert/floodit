import { Injectable } from '@angular/core';
import { CanActivate } from '@angular/router';
import { GameService } from './game.service';

@Injectable({
  providedIn: 'root'
})
export class ContinueCurrentGameGuard implements CanActivate {
  constructor(private readonly gameService: GameService) {}

  canActivate(): boolean {
    return !this.gameService.continueCurrentGame();
  }
}
