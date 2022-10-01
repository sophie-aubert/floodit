import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Profile } from 'src/app/profile/profile.model';
import { ProfileService } from 'src/app/profile/profile.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent {
  readonly profile: {
    name: string;
  };

  constructor(private readonly profileService: ProfileService) {
    this.profile = { name: '' };
  }

  submit(form: NgForm) {
    if (form.invalid) {
      return;
    }

    this.profileService.register(new Profile(this.profile.name));
  }
}
