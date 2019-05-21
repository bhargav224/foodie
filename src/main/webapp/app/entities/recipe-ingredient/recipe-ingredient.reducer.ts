import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IRecipeIngredient, defaultValue } from 'app/shared/model/recipe-ingredient.model';

export const ACTION_TYPES = {
  SEARCH_RECIPEINGREDIENTS: 'recipeIngredient/SEARCH_RECIPEINGREDIENTS',
  FETCH_RECIPEINGREDIENT_LIST: 'recipeIngredient/FETCH_RECIPEINGREDIENT_LIST',
  FETCH_RECIPEINGREDIENT: 'recipeIngredient/FETCH_RECIPEINGREDIENT',
  CREATE_RECIPEINGREDIENT: 'recipeIngredient/CREATE_RECIPEINGREDIENT',
  UPDATE_RECIPEINGREDIENT: 'recipeIngredient/UPDATE_RECIPEINGREDIENT',
  DELETE_RECIPEINGREDIENT: 'recipeIngredient/DELETE_RECIPEINGREDIENT',
  RESET: 'recipeIngredient/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IRecipeIngredient>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type RecipeIngredientState = Readonly<typeof initialState>;

// Reducer

export default (state: RecipeIngredientState = initialState, action): RecipeIngredientState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_RECIPEINGREDIENTS):
    case REQUEST(ACTION_TYPES.FETCH_RECIPEINGREDIENT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_RECIPEINGREDIENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_RECIPEINGREDIENT):
    case REQUEST(ACTION_TYPES.UPDATE_RECIPEINGREDIENT):
    case REQUEST(ACTION_TYPES.DELETE_RECIPEINGREDIENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_RECIPEINGREDIENTS):
    case FAILURE(ACTION_TYPES.FETCH_RECIPEINGREDIENT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_RECIPEINGREDIENT):
    case FAILURE(ACTION_TYPES.CREATE_RECIPEINGREDIENT):
    case FAILURE(ACTION_TYPES.UPDATE_RECIPEINGREDIENT):
    case FAILURE(ACTION_TYPES.DELETE_RECIPEINGREDIENT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_RECIPEINGREDIENTS):
    case SUCCESS(ACTION_TYPES.FETCH_RECIPEINGREDIENT_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_RECIPEINGREDIENT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_RECIPEINGREDIENT):
    case SUCCESS(ACTION_TYPES.UPDATE_RECIPEINGREDIENT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_RECIPEINGREDIENT):
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

const apiUrl = 'api/recipe-ingredients';
const apiSearchUrl = 'api/_search/recipe-ingredients';

// Actions

export const getSearchEntities: ICrudSearchAction<IRecipeIngredient> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_RECIPEINGREDIENTS,
  payload: axios.get<IRecipeIngredient>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<IRecipeIngredient> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_RECIPEINGREDIENT_LIST,
  payload: axios.get<IRecipeIngredient>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IRecipeIngredient> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_RECIPEINGREDIENT,
    payload: axios.get<IRecipeIngredient>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IRecipeIngredient> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_RECIPEINGREDIENT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IRecipeIngredient> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_RECIPEINGREDIENT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IRecipeIngredient> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_RECIPEINGREDIENT,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
