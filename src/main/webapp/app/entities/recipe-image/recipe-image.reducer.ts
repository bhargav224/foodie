import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IRecipeImage, defaultValue } from 'app/shared/model/recipe-image.model';

export const ACTION_TYPES = {
  SEARCH_RECIPEIMAGES: 'recipeImage/SEARCH_RECIPEIMAGES',
  FETCH_RECIPEIMAGE_LIST: 'recipeImage/FETCH_RECIPEIMAGE_LIST',
  FETCH_RECIPEIMAGE: 'recipeImage/FETCH_RECIPEIMAGE',
  CREATE_RECIPEIMAGE: 'recipeImage/CREATE_RECIPEIMAGE',
  UPDATE_RECIPEIMAGE: 'recipeImage/UPDATE_RECIPEIMAGE',
  DELETE_RECIPEIMAGE: 'recipeImage/DELETE_RECIPEIMAGE',
  RESET: 'recipeImage/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IRecipeImage>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type RecipeImageState = Readonly<typeof initialState>;

// Reducer

export default (state: RecipeImageState = initialState, action): RecipeImageState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_RECIPEIMAGES):
    case REQUEST(ACTION_TYPES.FETCH_RECIPEIMAGE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_RECIPEIMAGE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_RECIPEIMAGE):
    case REQUEST(ACTION_TYPES.UPDATE_RECIPEIMAGE):
    case REQUEST(ACTION_TYPES.DELETE_RECIPEIMAGE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_RECIPEIMAGES):
    case FAILURE(ACTION_TYPES.FETCH_RECIPEIMAGE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_RECIPEIMAGE):
    case FAILURE(ACTION_TYPES.CREATE_RECIPEIMAGE):
    case FAILURE(ACTION_TYPES.UPDATE_RECIPEIMAGE):
    case FAILURE(ACTION_TYPES.DELETE_RECIPEIMAGE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_RECIPEIMAGES):
    case SUCCESS(ACTION_TYPES.FETCH_RECIPEIMAGE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_RECIPEIMAGE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_RECIPEIMAGE):
    case SUCCESS(ACTION_TYPES.UPDATE_RECIPEIMAGE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_RECIPEIMAGE):
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

const apiUrl = 'api/recipe-images';
const apiSearchUrl = 'api/_search/recipe-images';

// Actions

export const getSearchEntities: ICrudSearchAction<IRecipeImage> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_RECIPEIMAGES,
  payload: axios.get<IRecipeImage>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<IRecipeImage> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_RECIPEIMAGE_LIST,
  payload: axios.get<IRecipeImage>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IRecipeImage> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_RECIPEIMAGE,
    payload: axios.get<IRecipeImage>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IRecipeImage> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_RECIPEIMAGE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IRecipeImage> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_RECIPEIMAGE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IRecipeImage> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_RECIPEIMAGE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
