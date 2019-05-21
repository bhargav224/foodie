import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IRecipeHasStep, defaultValue } from 'app/shared/model/recipe-has-step.model';

export const ACTION_TYPES = {
  SEARCH_RECIPEHASSTEPS: 'recipeHasStep/SEARCH_RECIPEHASSTEPS',
  FETCH_RECIPEHASSTEP_LIST: 'recipeHasStep/FETCH_RECIPEHASSTEP_LIST',
  FETCH_RECIPEHASSTEP: 'recipeHasStep/FETCH_RECIPEHASSTEP',
  CREATE_RECIPEHASSTEP: 'recipeHasStep/CREATE_RECIPEHASSTEP',
  UPDATE_RECIPEHASSTEP: 'recipeHasStep/UPDATE_RECIPEHASSTEP',
  DELETE_RECIPEHASSTEP: 'recipeHasStep/DELETE_RECIPEHASSTEP',
  RESET: 'recipeHasStep/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IRecipeHasStep>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type RecipeHasStepState = Readonly<typeof initialState>;

// Reducer

export default (state: RecipeHasStepState = initialState, action): RecipeHasStepState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_RECIPEHASSTEPS):
    case REQUEST(ACTION_TYPES.FETCH_RECIPEHASSTEP_LIST):
    case REQUEST(ACTION_TYPES.FETCH_RECIPEHASSTEP):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_RECIPEHASSTEP):
    case REQUEST(ACTION_TYPES.UPDATE_RECIPEHASSTEP):
    case REQUEST(ACTION_TYPES.DELETE_RECIPEHASSTEP):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_RECIPEHASSTEPS):
    case FAILURE(ACTION_TYPES.FETCH_RECIPEHASSTEP_LIST):
    case FAILURE(ACTION_TYPES.FETCH_RECIPEHASSTEP):
    case FAILURE(ACTION_TYPES.CREATE_RECIPEHASSTEP):
    case FAILURE(ACTION_TYPES.UPDATE_RECIPEHASSTEP):
    case FAILURE(ACTION_TYPES.DELETE_RECIPEHASSTEP):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_RECIPEHASSTEPS):
    case SUCCESS(ACTION_TYPES.FETCH_RECIPEHASSTEP_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_RECIPEHASSTEP):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_RECIPEHASSTEP):
    case SUCCESS(ACTION_TYPES.UPDATE_RECIPEHASSTEP):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_RECIPEHASSTEP):
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

const apiUrl = 'api/recipe-has-steps';
const apiSearchUrl = 'api/_search/recipe-has-steps';

// Actions

export const getSearchEntities: ICrudSearchAction<IRecipeHasStep> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_RECIPEHASSTEPS,
  payload: axios.get<IRecipeHasStep>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<IRecipeHasStep> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_RECIPEHASSTEP_LIST,
  payload: axios.get<IRecipeHasStep>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IRecipeHasStep> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_RECIPEHASSTEP,
    payload: axios.get<IRecipeHasStep>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IRecipeHasStep> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_RECIPEHASSTEP,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IRecipeHasStep> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_RECIPEHASSTEP,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IRecipeHasStep> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_RECIPEHASSTEP,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
