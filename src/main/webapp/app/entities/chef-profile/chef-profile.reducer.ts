import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IChefProfile, defaultValue } from 'app/shared/model/chef-profile.model';

export const ACTION_TYPES = {
  SEARCH_CHEFPROFILES: 'chefProfile/SEARCH_CHEFPROFILES',
  FETCH_CHEFPROFILE_LIST: 'chefProfile/FETCH_CHEFPROFILE_LIST',
  FETCH_CHEFPROFILE: 'chefProfile/FETCH_CHEFPROFILE',
  CREATE_CHEFPROFILE: 'chefProfile/CREATE_CHEFPROFILE',
  UPDATE_CHEFPROFILE: 'chefProfile/UPDATE_CHEFPROFILE',
  DELETE_CHEFPROFILE: 'chefProfile/DELETE_CHEFPROFILE',
  RESET: 'chefProfile/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IChefProfile>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ChefProfileState = Readonly<typeof initialState>;

// Reducer

export default (state: ChefProfileState = initialState, action): ChefProfileState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_CHEFPROFILES):
    case REQUEST(ACTION_TYPES.FETCH_CHEFPROFILE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CHEFPROFILE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CHEFPROFILE):
    case REQUEST(ACTION_TYPES.UPDATE_CHEFPROFILE):
    case REQUEST(ACTION_TYPES.DELETE_CHEFPROFILE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_CHEFPROFILES):
    case FAILURE(ACTION_TYPES.FETCH_CHEFPROFILE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CHEFPROFILE):
    case FAILURE(ACTION_TYPES.CREATE_CHEFPROFILE):
    case FAILURE(ACTION_TYPES.UPDATE_CHEFPROFILE):
    case FAILURE(ACTION_TYPES.DELETE_CHEFPROFILE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_CHEFPROFILES):
    case SUCCESS(ACTION_TYPES.FETCH_CHEFPROFILE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_CHEFPROFILE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CHEFPROFILE):
    case SUCCESS(ACTION_TYPES.UPDATE_CHEFPROFILE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CHEFPROFILE):
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

const apiUrl = 'api/chef-profiles';
const apiSearchUrl = 'api/_search/chef-profiles';

// Actions

export const getSearchEntities: ICrudSearchAction<IChefProfile> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_CHEFPROFILES,
  payload: axios.get<IChefProfile>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<IChefProfile> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_CHEFPROFILE_LIST,
  payload: axios.get<IChefProfile>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IChefProfile> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CHEFPROFILE,
    payload: axios.get<IChefProfile>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IChefProfile> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CHEFPROFILE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IChefProfile> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CHEFPROFILE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IChefProfile> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CHEFPROFILE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
