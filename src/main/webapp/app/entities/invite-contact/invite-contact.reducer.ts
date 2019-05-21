import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IInviteContact, defaultValue } from 'app/shared/model/invite-contact.model';

export const ACTION_TYPES = {
  SEARCH_INVITECONTACTS: 'inviteContact/SEARCH_INVITECONTACTS',
  FETCH_INVITECONTACT_LIST: 'inviteContact/FETCH_INVITECONTACT_LIST',
  FETCH_INVITECONTACT: 'inviteContact/FETCH_INVITECONTACT',
  CREATE_INVITECONTACT: 'inviteContact/CREATE_INVITECONTACT',
  UPDATE_INVITECONTACT: 'inviteContact/UPDATE_INVITECONTACT',
  DELETE_INVITECONTACT: 'inviteContact/DELETE_INVITECONTACT',
  RESET: 'inviteContact/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IInviteContact>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type InviteContactState = Readonly<typeof initialState>;

// Reducer

export default (state: InviteContactState = initialState, action): InviteContactState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_INVITECONTACTS):
    case REQUEST(ACTION_TYPES.FETCH_INVITECONTACT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_INVITECONTACT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_INVITECONTACT):
    case REQUEST(ACTION_TYPES.UPDATE_INVITECONTACT):
    case REQUEST(ACTION_TYPES.DELETE_INVITECONTACT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_INVITECONTACTS):
    case FAILURE(ACTION_TYPES.FETCH_INVITECONTACT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_INVITECONTACT):
    case FAILURE(ACTION_TYPES.CREATE_INVITECONTACT):
    case FAILURE(ACTION_TYPES.UPDATE_INVITECONTACT):
    case FAILURE(ACTION_TYPES.DELETE_INVITECONTACT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_INVITECONTACTS):
    case SUCCESS(ACTION_TYPES.FETCH_INVITECONTACT_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_INVITECONTACT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_INVITECONTACT):
    case SUCCESS(ACTION_TYPES.UPDATE_INVITECONTACT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_INVITECONTACT):
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

const apiUrl = 'api/invite-contacts';
const apiSearchUrl = 'api/_search/invite-contacts';

// Actions

export const getSearchEntities: ICrudSearchAction<IInviteContact> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_INVITECONTACTS,
  payload: axios.get<IInviteContact>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<IInviteContact> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_INVITECONTACT_LIST,
  payload: axios.get<IInviteContact>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IInviteContact> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_INVITECONTACT,
    payload: axios.get<IInviteContact>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IInviteContact> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_INVITECONTACT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IInviteContact> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_INVITECONTACT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IInviteContact> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_INVITECONTACT,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
