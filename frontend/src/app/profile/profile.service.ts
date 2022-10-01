import { Injectable, OnDestroy } from '@angular/core';
import { isString } from 'lodash-es';
import { BehaviorSubject, Observable, Subject, Subscription } from 'rxjs';
import { Profile } from './profile.model';

const storageKey = 'floodit.profile';

@Injectable({
  providedIn: 'root'
})
export class ProfileService implements OnDestroy {
  readonly profile$: Observable<Profile | undefined>;
  readonly #profile$: Subject<Profile | undefined>;
  readonly #subscription: Subscription;

  constructor() {
    this.#profile$ = new BehaviorSubject<Profile | undefined>(this.#load());
    this.profile$ = this.#profile$.asObservable();
    this.#subscription = this.profile$.subscribe(this.#save.bind(this));
  }

  register(profile: Profile): void {
    this.#profile$.next(profile);
  }

  unregister(): void {
    this.#profile$.next(undefined);
  }

  ngOnDestroy(): void {
    this.#subscription.unsubscribe();
  }

  #save(profile: Profile | undefined) {
    if (profile === undefined) {
      localStorage.removeItem(storageKey);
    } else {
      localStorage.setItem(storageKey, JSON.stringify({ name: profile.name }));
    }
  }

  #load(): Profile | undefined {
    const storedProfile = localStorage.getItem(storageKey);
    if (storedProfile === null) {
      return;
    }

    const parsedProfile = JSON.parse(storedProfile);
    const name = parsedProfile.name;

    return isString(name) ? new Profile(name) : undefined;
  }
}
