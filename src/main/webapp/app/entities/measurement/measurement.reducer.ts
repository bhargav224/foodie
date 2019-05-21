import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IMeasurement, defaultValue } from 'app/shared/model/measurement.model';

export const ACTION_TYPES = {
  SEARCH_MEASUREMENTS: 'measurement/SEARCH_MEASUREMENTS',
  FETCH_MEASUREMENT_LIST: 'measurement/FETCH_MEASUREMENT_LIST',
  FETCH_MEASUREMENT: 'measurement/FETCH_MEASUREMENT',
  CREATE_MEASUREMENT: 'measurement/CREATE_MEASUREMENT',
  UPDATE_MEASUREMENT: 'measurement/UPDATE_MEASUREMENT',
  DELETE_MEASUREMENT: 'measurement/DELETE_MEASUREMENT',
  RESET: 'measurement/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IMeasurement>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type MeasurementState = Readonly<typeof initialState>;

// Reducer

export default (state: MeasurementState = initialState, action): MeasurementState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_MEASUREMENTS):
    case REQUEST(ACTION_TYPES.FETCH_MEASUREMENT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_MEASUREMENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_MEASUREMENT):
    case REQUEST(ACTION_TYPES.UPDATE_MEASUREMENT):
    case REQUEST(ACTION_TYPES.DELETE_MEASUREMENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_MEASUREMENTS):
    case FAILURE(ACTION_TYPES.FETCH_MEASUREMENT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_MEASUREMENT):
    case FAILURE(ACTION_TYPES.CREATE_MEASUREMENT):
    case FAILURE(ACTION_TYPES.UPDATE_MEASUREMENT):
    case FAILURE(ACTION_TYPES.DELETE_MEASUREMENT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_MEASUREMENTS):
    case SUCCESS(ACTION_TYPES.FETCH_MEASUREMENT_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_MEASUREMENT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_MEASUREMENT):
    case SUCCESS(ACTION_TYPES.UPDATE_MEASUREMENT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_MEASUREMENT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/measurements';
const apiSearchUrl = 'api/_search/measurements';

// Actions

export const getSearchEntities: ICrudSearchAction<IMeasurement> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_MEASUREMENTS,
  payload: axios.get<IMeasurement>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<IMeasurement> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_MEASUREMENT_LIST,
  payload: axios.get<IMeasurement>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IMeasurement> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_MEASUREMENT,
    payload: axios.get<IMeasurement>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IMeasurement> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_MEASUREMENT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IMeasurement> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_MEASUREMENT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IMeasurement> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_MEASUREMENT,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
