import { Component, Input } from '@angular/core';
import { GameService } from 'src/app/games/game.service';
import { NavigationService } from 'src/app/navigation.service';

@Component({
  selector: 'app-new-game-card',
  templateUrl: './new-game-card.component.html',
  styleUrls: ['./new-game-card.component.css']
})
export class NewGameCardComponent {
  @Input()
  title!: string;

  @Input()
  description!: string;

  @Input()
  cardClass: string | string[] = [];

  @Input()
  boardWidth!: number;

  @Input()
  boardHeight!: number;

  @Input()
  numberOfColors!: number;

  @Input()
  maxMoves!: number;

  constructor(
    private readonly gameService: GameService,
    private readonly navigationService: NavigationService
  ) {}

  startGame(): void {
    this.gameService
      .startGame$({
        playerName: 'John Doe',
        boardWidth: this.boardWidth,
        boardHeight: this.boardHeight,
        numberOfColors: this.numberOfColors,
        maxMoves: this.maxMoves
      })
      .subscribe(() => this.navigationService.goToCurrentGame());
  }
}
