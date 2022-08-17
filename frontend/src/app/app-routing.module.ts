import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CurrentGameResolver } from './games/current-game.resolver';
import { CurrentGameComponent } from './pages/current-game/current-game.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';

const routes: Routes = [
  {
    path: '',
    component: DashboardComponent
  },
  {
    path: 'game',
    component: CurrentGameComponent,
    resolve: {
      currentGame: CurrentGameResolver
    }
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
