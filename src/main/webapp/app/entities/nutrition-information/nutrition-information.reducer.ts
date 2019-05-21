import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { INutritionInformation, defaultValue } from 'app/shared/model/nutrition-information.model';

export const ACTION_TYPES = {
  SEARCH_NUTRITIONINFORMATIONS: 'nutritionInformation/SEARCH_NUTRITIONINFORMATIONS',
  FETCH_NUTRITIONINFORMATION_LIST: 'nutritionInformation/FETCH_NUTRITIONINFORMATION_LIST',
  FETCH_NUTRITIONINFORMATION: 'nutritionInformation/FETCH_NUTRITIONINFORMATION',
  CREATE_NUTRITIONINFORMATION: 'nutritionInformation/CREATE_NUTRITIONINFORMATION',
  UPDATE_NUTRITIONINFORMATION: 'nutritionInformation/UPDATE_NUTRITIONINFORMATION',
  DELETE_NUTRITIONINFORMATION: 'nutritionInformation/DELETE_NUTRITIONINFORMATION',
  RESET: 'nutritionInformation/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<INutritionInformation>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type NutritionInformationState = Readonly<typeof initialState>;

// Reducer

export default (state: NutritionInformationState = initialState, action): NutritionInformationState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_NUTRITIONINFORMATIONS):
    case REQUEST(ACTION_TYPES.FETCH_NUTRITIONINFORMATION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_NUTRITIONINFORMATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_NUTRITIONINFORMATION):
    case REQUEST(ACTION_TYPES.UPDATE_NUTRITIONINFORMATION):
    case REQUEST(ACTION_TYPES.DELETE_NUTRITIONINFORMATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_NUTRITIONINFORMATIONS):
    case FAILURE(ACTION_TYPES.FETCH_NUTRITIONINFORMATION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_NUTRITIONINFORMATION):
    case FAILURE(ACTION_TYPES.CREATE_NUTRITIONINFORMATION):
    case FAILURE(ACTION_TYPES.UPDATE_NUTRITIONINFORMATION):
    case FAILURE(ACTION_TYPES.DELETE_NUTRITIONINFORMATION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_NUTRITIONINFORMATIONS):
    case SUCCESS(ACTION_TYPES.FETCH_NUTRITIONINFORMATION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_NUTRITIONINFORMATION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_NUTRITIONINFORMATION):
    case SUCCESS(ACTION_TYPES.UPDATE_NUTRITIONINFORMATION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_NUTRITIONINFORMATION):
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

const apiUrl = 'api/nutrition-informations';
const apiSearchUrl = 'api/_search/nutrition-informations';

// Actions

export const getSearchEntities: ICrudSearchAction<INutritionInformation> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_NUTRITIONINFORMATIONS,
  payload: axios.get<INutritionInformation>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<INutritionInformation> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_NUTRITIONINFORMATION_LIST,
  payload: axios.get<INutritionInformation>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<INutritionInformation> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_NUTRITIONINFORMATION,
    payload: axios.get<INutritionInformation>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<INutritionInformation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_NUTRITIONINFORMATION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<INutritionInformation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_NUTRITIONINFORMATION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<INutritionInformation> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_NUTRITIONINFORMATION,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
