import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICusine, defaultValue } from 'app/shared/model/cusine.model';

export const ACTION_TYPES = {
  SEARCH_CUSINES: 'cusine/SEARCH_CUSINES',
  FETCH_CUSINE_LIST: 'cusine/FETCH_CUSINE_LIST',
  FETCH_CUSINE: 'cusine/FETCH_CUSINE',
  CREATE_CUSINE: 'cusine/CREATE_CUSINE',
  UPDATE_CUSINE: 'cusine/UPDATE_CUSINE',
  DELETE_CUSINE: 'cusine/DELETE_CUSINE',
  RESET: 'cusine/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICusine>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type CusineState = Readonly<typeof initialState>;

// Reducer

export default (state: CusineState = initialState, action): CusineState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_CUSINES):
    case REQUEST(ACTION_TYPES.FETCH_CUSINE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CUSINE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CUSINE):
    case REQUEST(ACTION_TYPES.UPDATE_CUSINE):
    case REQUEST(ACTION_TYPES.DELETE_CUSINE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_CUSINES):
    case FAILURE(ACTION_TYPES.FETCH_CUSINE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CUSINE):
    case FAILURE(ACTION_TYPES.CREATE_CUSINE):
    case FAILURE(ACTION_TYPES.UPDATE_CUSINE):
    case FAILURE(ACTION_TYPES.DELETE_CUSINE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_CUSINES):
    case SUCCESS(ACTION_TYPES.FETCH_CUSINE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_CUSINE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CUSINE):
    case SUCCESS(ACTION_TYPES.UPDATE_CUSINE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CUSINE):
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

const apiUrl = 'api/cusines';
const apiSearchUrl = 'api/_search/cusines';

// Actions

export const getSearchEntities: ICrudSearchAction<ICusine> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_CUSINES,
  payload: axios.get<ICusine>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<ICusine> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_CUSINE_LIST,
  payload: axios.get<ICusine>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ICusine> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CUSINE,
    payload: axios.get<ICusine>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ICusine> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CUSINE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICusine> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CUSINE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICusine> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CUSINE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
