import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class NavigationService {
  constructor(private readonly router: Router) {}

  goToDashboard() {
    this.#navigateTo(['/']);
  }

  goToCurrentGame() {
    this.#navigateTo(['/play']);
  }

  #navigateTo(commands: any[]) {
    this.router.navigate(commands).catch(err => console.warn(err));
  }
}
