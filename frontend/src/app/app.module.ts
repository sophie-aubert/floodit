import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CurrentGameComponent } from './pages/current-game/current-game.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { RemoteDataComponent } from './utils/remote-data/remote-data.component';

@NgModule({
  bootstrap: [AppComponent],
  declarations: [
    AppComponent,
    DashboardComponent,
    CurrentGameComponent,
    RemoteDataComponent
  ],
  imports: [AppRoutingModule, BrowserModule, CommonModule, HttpClientModule],
  providers: []
})
export class AppModule {}
