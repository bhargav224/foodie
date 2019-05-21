import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IRecipeLike, defaultValue } from 'app/shared/model/recipe-like.model';

export const ACTION_TYPES = {
  SEARCH_RECIPELIKES: 'recipeLike/SEARCH_RECIPELIKES',
  FETCH_RECIPELIKE_LIST: 'recipeLike/FETCH_RECIPELIKE_LIST',
  FETCH_RECIPELIKE: 'recipeLike/FETCH_RECIPELIKE',
  CREATE_RECIPELIKE: 'recipeLike/CREATE_RECIPELIKE',
  UPDATE_RECIPELIKE: 'recipeLike/UPDATE_RECIPELIKE',
  DELETE_RECIPELIKE: 'recipeLike/DELETE_RECIPELIKE',
  RESET: 'recipeLike/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IRecipeLike>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type RecipeLikeState = Readonly<typeof initialState>;

// Reducer

export default (state: RecipeLikeState = initialState, action): RecipeLikeState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_RECIPELIKES):
    case REQUEST(ACTION_TYPES.FETCH_RECIPELIKE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_RECIPELIKE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_RECIPELIKE):
    case REQUEST(ACTION_TYPES.UPDATE_RECIPELIKE):
    case REQUEST(ACTION_TYPES.DELETE_RECIPELIKE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_RECIPELIKES):
    case FAILURE(ACTION_TYPES.FETCH_RECIPELIKE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_RECIPELIKE):
    case FAILURE(ACTION_TYPES.CREATE_RECIPELIKE):
    case FAILURE(ACTION_TYPES.UPDATE_RECIPELIKE):
    case FAILURE(ACTION_TYPES.DELETE_RECIPELIKE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_RECIPELIKES):
    case SUCCESS(ACTION_TYPES.FETCH_RECIPELIKE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_RECIPELIKE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_RECIPELIKE):
    case SUCCESS(ACTION_TYPES.UPDATE_RECIPELIKE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_RECIPELIKE):
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

const apiUrl = 'api/recipe-likes';
const apiSearchUrl = 'api/_search/recipe-likes';

// Actions

export const getSearchEntities: ICrudSearchAction<IRecipeLike> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_RECIPELIKES,
  payload: axios.get<IRecipeLike>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<IRecipeLike> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_RECIPELIKE_LIST,
  payload: axios.get<IRecipeLike>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IRecipeLike> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_RECIPELIKE,
    payload: axios.get<IRecipeLike>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IRecipeLike> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_RECIPELIKE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IRecipeLike> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_RECIPELIKE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IRecipeLike> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_RECIPELIKE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
