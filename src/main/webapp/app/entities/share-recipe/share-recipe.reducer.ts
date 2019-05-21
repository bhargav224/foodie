import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IShareRecipe, defaultValue } from 'app/shared/model/share-recipe.model';

export const ACTION_TYPES = {
  SEARCH_SHARERECIPES: 'shareRecipe/SEARCH_SHARERECIPES',
  FETCH_SHARERECIPE_LIST: 'shareRecipe/FETCH_SHARERECIPE_LIST',
  FETCH_SHARERECIPE: 'shareRecipe/FETCH_SHARERECIPE',
  CREATE_SHARERECIPE: 'shareRecipe/CREATE_SHARERECIPE',
  UPDATE_SHARERECIPE: 'shareRecipe/UPDATE_SHARERECIPE',
  DELETE_SHARERECIPE: 'shareRecipe/DELETE_SHARERECIPE',
  RESET: 'shareRecipe/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IShareRecipe>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ShareRecipeState = Readonly<typeof initialState>;

// Reducer

export default (state: ShareRecipeState = initialState, action): ShareRecipeState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_SHARERECIPES):
    case REQUEST(ACTION_TYPES.FETCH_SHARERECIPE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SHARERECIPE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SHARERECIPE):
    case REQUEST(ACTION_TYPES.UPDATE_SHARERECIPE):
    case REQUEST(ACTION_TYPES.DELETE_SHARERECIPE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_SHARERECIPES):
    case FAILURE(ACTION_TYPES.FETCH_SHARERECIPE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SHARERECIPE):
    case FAILURE(ACTION_TYPES.CREATE_SHARERECIPE):
    case FAILURE(ACTION_TYPES.UPDATE_SHARERECIPE):
    case FAILURE(ACTION_TYPES.DELETE_SHARERECIPE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_SHARERECIPES):
    case SUCCESS(ACTION_TYPES.FETCH_SHARERECIPE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SHARERECIPE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SHARERECIPE):
    case SUCCESS(ACTION_TYPES.UPDATE_SHARERECIPE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SHARERECIPE):
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

const apiUrl = 'api/share-recipes';
const apiSearchUrl = 'api/_search/share-recipes';

// Actions

export const getSearchEntities: ICrudSearchAction<IShareRecipe> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_SHARERECIPES,
  payload: axios.get<IShareRecipe>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<IShareRecipe> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_SHARERECIPE_LIST,
  payload: axios.get<IShareRecipe>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IShareRecipe> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SHARERECIPE,
    payload: axios.get<IShareRecipe>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IShareRecipe> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SHARERECIPE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IShareRecipe> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SHARERECIPE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IShareRecipe> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SHARERECIPE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
