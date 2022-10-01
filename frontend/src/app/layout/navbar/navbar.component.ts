import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { Profile } from 'src/app/profile/profile.model';
import { ProfileService } from 'src/app/profile/profile.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  readonly profile$: Observable<Profile | undefined>;

  constructor(private readonly profileService: ProfileService) {
    this.profile$ = profileService.profile$;
  }

  unregister(): void {
    this.profileService.unregister();
  }
}
