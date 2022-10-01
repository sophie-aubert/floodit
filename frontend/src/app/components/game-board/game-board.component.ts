import { Component, Input } from '@angular/core';
import { colors } from 'src/app/games/game.constants';

@Component({
  selector: 'app-game-board',
  templateUrl: './game-board.component.html',
  styleUrls: ['./game-board.component.css']
})
export class GameBoardComponent {
  @Input()
  board!: number[][];

  @Input()
  palette: string[] = colors;
}
