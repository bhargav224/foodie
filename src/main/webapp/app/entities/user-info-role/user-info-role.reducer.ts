import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IUserInfoRole, defaultValue } from 'app/shared/model/user-info-role.model';

export const ACTION_TYPES = {
  SEARCH_USERINFOROLES: 'userInfoRole/SEARCH_USERINFOROLES',
  FETCH_USERINFOROLE_LIST: 'userInfoRole/FETCH_USERINFOROLE_LIST',
  FETCH_USERINFOROLE: 'userInfoRole/FETCH_USERINFOROLE',
  CREATE_USERINFOROLE: 'userInfoRole/CREATE_USERINFOROLE',
  UPDATE_USERINFOROLE: 'userInfoRole/UPDATE_USERINFOROLE',
  DELETE_USERINFOROLE: 'userInfoRole/DELETE_USERINFOROLE',
  RESET: 'userInfoRole/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IUserInfoRole>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type UserInfoRoleState = Readonly<typeof initialState>;

// Reducer

export default (state: UserInfoRoleState = initialState, action): UserInfoRoleState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_USERINFOROLES):
    case REQUEST(ACTION_TYPES.FETCH_USERINFOROLE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_USERINFOROLE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_USERINFOROLE):
    case REQUEST(ACTION_TYPES.UPDATE_USERINFOROLE):
    case REQUEST(ACTION_TYPES.DELETE_USERINFOROLE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_USERINFOROLES):
    case FAILURE(ACTION_TYPES.FETCH_USERINFOROLE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_USERINFOROLE):
    case FAILURE(ACTION_TYPES.CREATE_USERINFOROLE):
    case FAILURE(ACTION_TYPES.UPDATE_USERINFOROLE):
    case FAILURE(ACTION_TYPES.DELETE_USERINFOROLE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_USERINFOROLES):
    case SUCCESS(ACTION_TYPES.FETCH_USERINFOROLE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_USERINFOROLE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_USERINFOROLE):
    case SUCCESS(ACTION_TYPES.UPDATE_USERINFOROLE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_USERINFOROLE):
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

const apiUrl = 'api/user-info-roles';
const apiSearchUrl = 'api/_search/user-info-roles';

// Actions

export const getSearchEntities: ICrudSearchAction<IUserInfoRole> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_USERINFOROLES,
  payload: axios.get<IUserInfoRole>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<IUserInfoRole> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_USERINFOROLE_LIST,
  payload: axios.get<IUserInfoRole>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IUserInfoRole> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_USERINFOROLE,
    payload: axios.get<IUserInfoRole>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IUserInfoRole> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_USERINFOROLE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IUserInfoRole> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_USERINFOROLE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IUserInfoRole> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_USERINFOROLE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
