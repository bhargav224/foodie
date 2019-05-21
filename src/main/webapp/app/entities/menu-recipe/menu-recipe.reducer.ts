import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IMenuRecipe, defaultValue } from 'app/shared/model/menu-recipe.model';

export const ACTION_TYPES = {
  SEARCH_MENURECIPES: 'menuRecipe/SEARCH_MENURECIPES',
  FETCH_MENURECIPE_LIST: 'menuRecipe/FETCH_MENURECIPE_LIST',
  FETCH_MENURECIPE: 'menuRecipe/FETCH_MENURECIPE',
  CREATE_MENURECIPE: 'menuRecipe/CREATE_MENURECIPE',
  UPDATE_MENURECIPE: 'menuRecipe/UPDATE_MENURECIPE',
  DELETE_MENURECIPE: 'menuRecipe/DELETE_MENURECIPE',
  RESET: 'menuRecipe/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IMenuRecipe>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type MenuRecipeState = Readonly<typeof initialState>;

// Reducer

export default (state: MenuRecipeState = initialState, action): MenuRecipeState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_MENURECIPES):
    case REQUEST(ACTION_TYPES.FETCH_MENURECIPE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_MENURECIPE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_MENURECIPE):
    case REQUEST(ACTION_TYPES.UPDATE_MENURECIPE):
    case REQUEST(ACTION_TYPES.DELETE_MENURECIPE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_MENURECIPES):
    case FAILURE(ACTION_TYPES.FETCH_MENURECIPE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_MENURECIPE):
    case FAILURE(ACTION_TYPES.CREATE_MENURECIPE):
    case FAILURE(ACTION_TYPES.UPDATE_MENURECIPE):
    case FAILURE(ACTION_TYPES.DELETE_MENURECIPE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_MENURECIPES):
    case SUCCESS(ACTION_TYPES.FETCH_MENURECIPE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_MENURECIPE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_MENURECIPE):
    case SUCCESS(ACTION_TYPES.UPDATE_MENURECIPE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_MENURECIPE):
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

const apiUrl = 'api/menu-recipes';
const apiSearchUrl = 'api/_search/menu-recipes';

// Actions

export const getSearchEntities: ICrudSearchAction<IMenuRecipe> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_MENURECIPES,
  payload: axios.get<IMenuRecipe>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<IMenuRecipe> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_MENURECIPE_LIST,
  payload: axios.get<IMenuRecipe>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IMenuRecipe> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_MENURECIPE,
    payload: axios.get<IMenuRecipe>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IMenuRecipe> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_MENURECIPE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IMenuRecipe> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_MENURECIPE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IMenuRecipe> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_MENURECIPE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
