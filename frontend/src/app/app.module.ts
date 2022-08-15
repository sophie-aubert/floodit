import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';

@NgModule({
  bootstrap: [AppComponent],
  declarations: [AppComponent, DashboardComponent],
  imports: [AppRoutingModule, BrowserModule, CommonModule, HttpClientModule],
  providers: []
})
export class AppModule {}
