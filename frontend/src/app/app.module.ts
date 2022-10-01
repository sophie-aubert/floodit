import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LayoutComponent } from './layout/layout.component';
import { NavbarComponent } from './layout/navbar/navbar.component';
import { CurrentGameComponent } from './pages/current-game/current-game.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { NewGameCardComponent } from './pages/dashboard/new-game-card/new-game-card.component';
import { PreviousGamesComponent } from './pages/dashboard/previous-games/previous-games.component';

@NgModule({
  bootstrap: [AppComponent],
  declarations: [
    AppComponent,
    DashboardComponent,
    CurrentGameComponent,
    LayoutComponent,
    NavbarComponent,
    NewGameCardComponent,
    PreviousGamesComponent
  ],
  imports: [AppRoutingModule, BrowserModule, CommonModule, HttpClientModule],
  providers: []
})
export class AppModule {}
