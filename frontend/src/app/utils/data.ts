export type DataNotLoaded = {
  readonly state: 'notLoaded';
};

export type LoadingData = {
  readonly state: 'loading';
};

export type LoadedData<T> = {
  readonly state: 'loaded';
  readonly data: T;
};

export type DataLoadingError = {
  readonly state: 'error';
  readonly error: unknown;
};

export type RefreshingData<T> = {
  readonly state: 'refreshing';
  readonly data: T;
};

export type RemoteData<T> =
  | DataNotLoaded
  | LoadingData
  | LoadedData<T>
  | DataLoadingError
  | RefreshingData<T>;
