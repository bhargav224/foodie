import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IUserRating, defaultValue } from 'app/shared/model/user-rating.model';

export const ACTION_TYPES = {
  SEARCH_USERRATINGS: 'userRating/SEARCH_USERRATINGS',
  FETCH_USERRATING_LIST: 'userRating/FETCH_USERRATING_LIST',
  FETCH_USERRATING: 'userRating/FETCH_USERRATING',
  CREATE_USERRATING: 'userRating/CREATE_USERRATING',
  UPDATE_USERRATING: 'userRating/UPDATE_USERRATING',
  DELETE_USERRATING: 'userRating/DELETE_USERRATING',
  RESET: 'userRating/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IUserRating>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type UserRatingState = Readonly<typeof initialState>;

// Reducer

export default (state: UserRatingState = initialState, action): UserRatingState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_USERRATINGS):
    case REQUEST(ACTION_TYPES.FETCH_USERRATING_LIST):
    case REQUEST(ACTION_TYPES.FETCH_USERRATING):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_USERRATING):
    case REQUEST(ACTION_TYPES.UPDATE_USERRATING):
    case REQUEST(ACTION_TYPES.DELETE_USERRATING):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_USERRATINGS):
    case FAILURE(ACTION_TYPES.FETCH_USERRATING_LIST):
    case FAILURE(ACTION_TYPES.FETCH_USERRATING):
    case FAILURE(ACTION_TYPES.CREATE_USERRATING):
    case FAILURE(ACTION_TYPES.UPDATE_USERRATING):
    case FAILURE(ACTION_TYPES.DELETE_USERRATING):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_USERRATINGS):
    case SUCCESS(ACTION_TYPES.FETCH_USERRATING_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_USERRATING):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_USERRATING):
    case SUCCESS(ACTION_TYPES.UPDATE_USERRATING):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_USERRATING):
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

const apiUrl = 'api/user-ratings';
const apiSearchUrl = 'api/_search/user-ratings';

// Actions

export const getSearchEntities: ICrudSearchAction<IUserRating> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_USERRATINGS,
  payload: axios.get<IUserRating>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<IUserRating> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_USERRATING_LIST,
  payload: axios.get<IUserRating>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IUserRating> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_USERRATING,
    payload: axios.get<IUserRating>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IUserRating> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_USERRATING,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IUserRating> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_USERRATING,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IUserRating> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_USERRATING,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
