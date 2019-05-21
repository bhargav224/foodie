import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IRecipeRating, defaultValue } from 'app/shared/model/recipe-rating.model';

export const ACTION_TYPES = {
  SEARCH_RECIPERATINGS: 'recipeRating/SEARCH_RECIPERATINGS',
  FETCH_RECIPERATING_LIST: 'recipeRating/FETCH_RECIPERATING_LIST',
  FETCH_RECIPERATING: 'recipeRating/FETCH_RECIPERATING',
  CREATE_RECIPERATING: 'recipeRating/CREATE_RECIPERATING',
  UPDATE_RECIPERATING: 'recipeRating/UPDATE_RECIPERATING',
  DELETE_RECIPERATING: 'recipeRating/DELETE_RECIPERATING',
  RESET: 'recipeRating/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IRecipeRating>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type RecipeRatingState = Readonly<typeof initialState>;

// Reducer

export default (state: RecipeRatingState = initialState, action): RecipeRatingState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_RECIPERATINGS):
    case REQUEST(ACTION_TYPES.FETCH_RECIPERATING_LIST):
    case REQUEST(ACTION_TYPES.FETCH_RECIPERATING):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_RECIPERATING):
    case REQUEST(ACTION_TYPES.UPDATE_RECIPERATING):
    case REQUEST(ACTION_TYPES.DELETE_RECIPERATING):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_RECIPERATINGS):
    case FAILURE(ACTION_TYPES.FETCH_RECIPERATING_LIST):
    case FAILURE(ACTION_TYPES.FETCH_RECIPERATING):
    case FAILURE(ACTION_TYPES.CREATE_RECIPERATING):
    case FAILURE(ACTION_TYPES.UPDATE_RECIPERATING):
    case FAILURE(ACTION_TYPES.DELETE_RECIPERATING):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_RECIPERATINGS):
    case SUCCESS(ACTION_TYPES.FETCH_RECIPERATING_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_RECIPERATING):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_RECIPERATING):
    case SUCCESS(ACTION_TYPES.UPDATE_RECIPERATING):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_RECIPERATING):
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

const apiUrl = 'api/recipe-ratings';
const apiSearchUrl = 'api/_search/recipe-ratings';

// Actions

export const getSearchEntities: ICrudSearchAction<IRecipeRating> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_RECIPERATINGS,
  payload: axios.get<IRecipeRating>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<IRecipeRating> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_RECIPERATING_LIST,
  payload: axios.get<IRecipeRating>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IRecipeRating> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_RECIPERATING,
    payload: axios.get<IRecipeRating>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IRecipeRating> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_RECIPERATING,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IRecipeRating> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_RECIPERATING,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IRecipeRating> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_RECIPERATING,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
