import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ContinueCurrentGameGuard } from './games/continue-current-game.guard';
import { LayoutComponent } from './layout/layout.component';
import { CurrentGameComponent } from './pages/current-game/current-game.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';

const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      {
        path: '',
        component: DashboardComponent,
        canActivate: [ContinueCurrentGameGuard]
      },
      {
        path: 'play',
        component: CurrentGameComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
