import { Component, ContentChild, Input, TemplateRef } from '@angular/core';
import { RemoteData, RemoteDateState } from './remote-data.model';

@Component({
  selector: 'app-remote-data',
  templateUrl: './remote-data.component.html',
  styleUrls: ['./remote-data.component.css']
})
export class RemoteDataComponent<T> {
  @Input()
  remoteData?: RemoteData<T> | null;

  @ContentChild('loading', { static: true })
  loadingTemplate!: TemplateRef<unknown>;

  @ContentChild('loaded', { static: true })
  loadedTemplate!: TemplateRef<unknown>;

  readonly RemoteDataState = RemoteDateState;
}
