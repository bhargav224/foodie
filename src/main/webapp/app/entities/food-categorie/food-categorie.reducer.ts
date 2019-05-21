import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IFoodCategorie, defaultValue } from 'app/shared/model/food-categorie.model';

export const ACTION_TYPES = {
  SEARCH_FOODCATEGORIES: 'foodCategorie/SEARCH_FOODCATEGORIES',
  FETCH_FOODCATEGORIE_LIST: 'foodCategorie/FETCH_FOODCATEGORIE_LIST',
  FETCH_FOODCATEGORIE: 'foodCategorie/FETCH_FOODCATEGORIE',
  CREATE_FOODCATEGORIE: 'foodCategorie/CREATE_FOODCATEGORIE',
  UPDATE_FOODCATEGORIE: 'foodCategorie/UPDATE_FOODCATEGORIE',
  DELETE_FOODCATEGORIE: 'foodCategorie/DELETE_FOODCATEGORIE',
  RESET: 'foodCategorie/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IFoodCategorie>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type FoodCategorieState = Readonly<typeof initialState>;

// Reducer

export default (state: FoodCategorieState = initialState, action): FoodCategorieState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_FOODCATEGORIES):
    case REQUEST(ACTION_TYPES.FETCH_FOODCATEGORIE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_FOODCATEGORIE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_FOODCATEGORIE):
    case REQUEST(ACTION_TYPES.UPDATE_FOODCATEGORIE):
    case REQUEST(ACTION_TYPES.DELETE_FOODCATEGORIE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_FOODCATEGORIES):
    case FAILURE(ACTION_TYPES.FETCH_FOODCATEGORIE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_FOODCATEGORIE):
    case FAILURE(ACTION_TYPES.CREATE_FOODCATEGORIE):
    case FAILURE(ACTION_TYPES.UPDATE_FOODCATEGORIE):
    case FAILURE(ACTION_TYPES.DELETE_FOODCATEGORIE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_FOODCATEGORIES):
    case SUCCESS(ACTION_TYPES.FETCH_FOODCATEGORIE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_FOODCATEGORIE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_FOODCATEGORIE):
    case SUCCESS(ACTION_TYPES.UPDATE_FOODCATEGORIE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_FOODCATEGORIE):
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

const apiUrl = 'api/food-categories';
const apiSearchUrl = 'api/_search/food-categories';

// Actions

export const getSearchEntities: ICrudSearchAction<IFoodCategorie> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_FOODCATEGORIES,
  payload: axios.get<IFoodCategorie>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<IFoodCategorie> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_FOODCATEGORIE_LIST,
  payload: axios.get<IFoodCategorie>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IFoodCategorie> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_FOODCATEGORIE,
    payload: axios.get<IFoodCategorie>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IFoodCategorie> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_FOODCATEGORIE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IFoodCategorie> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_FOODCATEGORIE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IFoodCategorie> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_FOODCATEGORIE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
