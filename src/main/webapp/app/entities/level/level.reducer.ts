import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ILevel, defaultValue } from 'app/shared/model/level.model';

export const ACTION_TYPES = {
  SEARCH_LEVELS: 'level/SEARCH_LEVELS',
  FETCH_LEVEL_LIST: 'level/FETCH_LEVEL_LIST',
  FETCH_LEVEL: 'level/FETCH_LEVEL',
  CREATE_LEVEL: 'level/CREATE_LEVEL',
  UPDATE_LEVEL: 'level/UPDATE_LEVEL',
  DELETE_LEVEL: 'level/DELETE_LEVEL',
  RESET: 'level/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ILevel>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type LevelState = Readonly<typeof initialState>;

// Reducer

export default (state: LevelState = initialState, action): LevelState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_LEVELS):
    case REQUEST(ACTION_TYPES.FETCH_LEVEL_LIST):
    case REQUEST(ACTION_TYPES.FETCH_LEVEL):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_LEVEL):
    case REQUEST(ACTION_TYPES.UPDATE_LEVEL):
    case REQUEST(ACTION_TYPES.DELETE_LEVEL):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_LEVELS):
    case FAILURE(ACTION_TYPES.FETCH_LEVEL_LIST):
    case FAILURE(ACTION_TYPES.FETCH_LEVEL):
    case FAILURE(ACTION_TYPES.CREATE_LEVEL):
    case FAILURE(ACTION_TYPES.UPDATE_LEVEL):
    case FAILURE(ACTION_TYPES.DELETE_LEVEL):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_LEVELS):
    case SUCCESS(ACTION_TYPES.FETCH_LEVEL_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_LEVEL):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_LEVEL):
    case SUCCESS(ACTION_TYPES.UPDATE_LEVEL):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_LEVEL):
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

const apiUrl = 'api/levels';
const apiSearchUrl = 'api/_search/levels';

// Actions

export const getSearchEntities: ICrudSearchAction<ILevel> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_LEVELS,
  payload: axios.get<ILevel>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<ILevel> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_LEVEL_LIST,
  payload: axios.get<ILevel>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ILevel> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_LEVEL,
    payload: axios.get<ILevel>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ILevel> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_LEVEL,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ILevel> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_LEVEL,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ILevel> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_LEVEL,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
