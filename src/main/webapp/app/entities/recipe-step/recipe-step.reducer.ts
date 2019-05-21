import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IRecipeStep, defaultValue } from 'app/shared/model/recipe-step.model';

export const ACTION_TYPES = {
  SEARCH_RECIPESTEPS: 'recipeStep/SEARCH_RECIPESTEPS',
  FETCH_RECIPESTEP_LIST: 'recipeStep/FETCH_RECIPESTEP_LIST',
  FETCH_RECIPESTEP: 'recipeStep/FETCH_RECIPESTEP',
  CREATE_RECIPESTEP: 'recipeStep/CREATE_RECIPESTEP',
  UPDATE_RECIPESTEP: 'recipeStep/UPDATE_RECIPESTEP',
  DELETE_RECIPESTEP: 'recipeStep/DELETE_RECIPESTEP',
  RESET: 'recipeStep/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IRecipeStep>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type RecipeStepState = Readonly<typeof initialState>;

// Reducer

export default (state: RecipeStepState = initialState, action): RecipeStepState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_RECIPESTEPS):
    case REQUEST(ACTION_TYPES.FETCH_RECIPESTEP_LIST):
    case REQUEST(ACTION_TYPES.FETCH_RECIPESTEP):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_RECIPESTEP):
    case REQUEST(ACTION_TYPES.UPDATE_RECIPESTEP):
    case REQUEST(ACTION_TYPES.DELETE_RECIPESTEP):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_RECIPESTEPS):
    case FAILURE(ACTION_TYPES.FETCH_RECIPESTEP_LIST):
    case FAILURE(ACTION_TYPES.FETCH_RECIPESTEP):
    case FAILURE(ACTION_TYPES.CREATE_RECIPESTEP):
    case FAILURE(ACTION_TYPES.UPDATE_RECIPESTEP):
    case FAILURE(ACTION_TYPES.DELETE_RECIPESTEP):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_RECIPESTEPS):
    case SUCCESS(ACTION_TYPES.FETCH_RECIPESTEP_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_RECIPESTEP):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_RECIPESTEP):
    case SUCCESS(ACTION_TYPES.UPDATE_RECIPESTEP):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_RECIPESTEP):
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

const apiUrl = 'api/recipe-steps';
const apiSearchUrl = 'api/_search/recipe-steps';

// Actions

export const getSearchEntities: ICrudSearchAction<IRecipeStep> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_RECIPESTEPS,
  payload: axios.get<IRecipeStep>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<IRecipeStep> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_RECIPESTEP_LIST,
  payload: axios.get<IRecipeStep>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IRecipeStep> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_RECIPESTEP,
    payload: axios.get<IRecipeStep>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IRecipeStep> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_RECIPESTEP,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IRecipeStep> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_RECIPESTEP,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IRecipeStep> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_RECIPESTEP,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
