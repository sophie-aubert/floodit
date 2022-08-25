export enum RemoteDateState {
  Loading,
  Loaded,
  Error
}

export type LoadingData = {
  readonly state: RemoteDateState.Loading;
};

export type LoadedData<T> = {
  readonly state: RemoteDateState.Loaded;
  readonly data: T;
};

export type DataLoadingError = {
  readonly state: RemoteDateState.Error;
  readonly error: unknown;
};

export type RemoteData<T> = LoadingData | LoadedData<T> | DataLoadingError;

export function loading(): LoadingData {
  return { state: RemoteDateState.Loading };
}

export function loaded<T>(data: T): LoadedData<T> {
  return { state: RemoteDateState.Loaded, data };
}

export function error(err: unknown): DataLoadingError {
  return { state: RemoteDateState.Error, error: err };
}
