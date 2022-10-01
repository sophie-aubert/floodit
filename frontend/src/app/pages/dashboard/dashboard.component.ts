import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { Profile } from 'src/app/profile/profile.model';
import { ProfileService } from 'src/app/profile/profile.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {
  readonly profile$: Observable<Profile | undefined>;

  constructor(profileService: ProfileService) {
    this.profile$ = profileService.profile$;
  }
}
