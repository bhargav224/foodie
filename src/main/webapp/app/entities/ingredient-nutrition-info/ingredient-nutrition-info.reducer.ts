import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IIngredientNutritionInfo, defaultValue } from 'app/shared/model/ingredient-nutrition-info.model';

export const ACTION_TYPES = {
  SEARCH_INGREDIENTNUTRITIONINFOS: 'ingredientNutritionInfo/SEARCH_INGREDIENTNUTRITIONINFOS',
  FETCH_INGREDIENTNUTRITIONINFO_LIST: 'ingredientNutritionInfo/FETCH_INGREDIENTNUTRITIONINFO_LIST',
  FETCH_INGREDIENTNUTRITIONINFO: 'ingredientNutritionInfo/FETCH_INGREDIENTNUTRITIONINFO',
  CREATE_INGREDIENTNUTRITIONINFO: 'ingredientNutritionInfo/CREATE_INGREDIENTNUTRITIONINFO',
  UPDATE_INGREDIENTNUTRITIONINFO: 'ingredientNutritionInfo/UPDATE_INGREDIENTNUTRITIONINFO',
  DELETE_INGREDIENTNUTRITIONINFO: 'ingredientNutritionInfo/DELETE_INGREDIENTNUTRITIONINFO',
  RESET: 'ingredientNutritionInfo/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IIngredientNutritionInfo>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type IngredientNutritionInfoState = Readonly<typeof initialState>;

// Reducer

export default (state: IngredientNutritionInfoState = initialState, action): IngredientNutritionInfoState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_INGREDIENTNUTRITIONINFOS):
    case REQUEST(ACTION_TYPES.FETCH_INGREDIENTNUTRITIONINFO_LIST):
    case REQUEST(ACTION_TYPES.FETCH_INGREDIENTNUTRITIONINFO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_INGREDIENTNUTRITIONINFO):
    case REQUEST(ACTION_TYPES.UPDATE_INGREDIENTNUTRITIONINFO):
    case REQUEST(ACTION_TYPES.DELETE_INGREDIENTNUTRITIONINFO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_INGREDIENTNUTRITIONINFOS):
    case FAILURE(ACTION_TYPES.FETCH_INGREDIENTNUTRITIONINFO_LIST):
    case FAILURE(ACTION_TYPES.FETCH_INGREDIENTNUTRITIONINFO):
    case FAILURE(ACTION_TYPES.CREATE_INGREDIENTNUTRITIONINFO):
    case FAILURE(ACTION_TYPES.UPDATE_INGREDIENTNUTRITIONINFO):
    case FAILURE(ACTION_TYPES.DELETE_INGREDIENTNUTRITIONINFO):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_INGREDIENTNUTRITIONINFOS):
    case SUCCESS(ACTION_TYPES.FETCH_INGREDIENTNUTRITIONINFO_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_INGREDIENTNUTRITIONINFO):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_INGREDIENTNUTRITIONINFO):
    case SUCCESS(ACTION_TYPES.UPDATE_INGREDIENTNUTRITIONINFO):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_INGREDIENTNUTRITIONINFO):
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

const apiUrl = 'api/ingredient-nutrition-infos';
const apiSearchUrl = 'api/_search/ingredient-nutrition-infos';

// Actions

export const getSearchEntities: ICrudSearchAction<IIngredientNutritionInfo> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_INGREDIENTNUTRITIONINFOS,
  payload: axios.get<IIngredientNutritionInfo>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<IIngredientNutritionInfo> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_INGREDIENTNUTRITIONINFO_LIST,
  payload: axios.get<IIngredientNutritionInfo>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IIngredientNutritionInfo> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_INGREDIENTNUTRITIONINFO,
    payload: axios.get<IIngredientNutritionInfo>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IIngredientNutritionInfo> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_INGREDIENTNUTRITIONINFO,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IIngredientNutritionInfo> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_INGREDIENTNUTRITIONINFO,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IIngredientNutritionInfo> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_INGREDIENTNUTRITIONINFO,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
