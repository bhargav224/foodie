import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IInviteEmail, defaultValue } from 'app/shared/model/invite-email.model';

export const ACTION_TYPES = {
  SEARCH_INVITEEMAILS: 'inviteEmail/SEARCH_INVITEEMAILS',
  FETCH_INVITEEMAIL_LIST: 'inviteEmail/FETCH_INVITEEMAIL_LIST',
  FETCH_INVITEEMAIL: 'inviteEmail/FETCH_INVITEEMAIL',
  CREATE_INVITEEMAIL: 'inviteEmail/CREATE_INVITEEMAIL',
  UPDATE_INVITEEMAIL: 'inviteEmail/UPDATE_INVITEEMAIL',
  DELETE_INVITEEMAIL: 'inviteEmail/DELETE_INVITEEMAIL',
  RESET: 'inviteEmail/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IInviteEmail>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type InviteEmailState = Readonly<typeof initialState>;

// Reducer

export default (state: InviteEmailState = initialState, action): InviteEmailState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_INVITEEMAILS):
    case REQUEST(ACTION_TYPES.FETCH_INVITEEMAIL_LIST):
    case REQUEST(ACTION_TYPES.FETCH_INVITEEMAIL):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_INVITEEMAIL):
    case REQUEST(ACTION_TYPES.UPDATE_INVITEEMAIL):
    case REQUEST(ACTION_TYPES.DELETE_INVITEEMAIL):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_INVITEEMAILS):
    case FAILURE(ACTION_TYPES.FETCH_INVITEEMAIL_LIST):
    case FAILURE(ACTION_TYPES.FETCH_INVITEEMAIL):
    case FAILURE(ACTION_TYPES.CREATE_INVITEEMAIL):
    case FAILURE(ACTION_TYPES.UPDATE_INVITEEMAIL):
    case FAILURE(ACTION_TYPES.DELETE_INVITEEMAIL):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_INVITEEMAILS):
    case SUCCESS(ACTION_TYPES.FETCH_INVITEEMAIL_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_INVITEEMAIL):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_INVITEEMAIL):
    case SUCCESS(ACTION_TYPES.UPDATE_INVITEEMAIL):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_INVITEEMAIL):
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

const apiUrl = 'api/invite-emails';
const apiSearchUrl = 'api/_search/invite-emails';

// Actions

export const getSearchEntities: ICrudSearchAction<IInviteEmail> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_INVITEEMAILS,
  payload: axios.get<IInviteEmail>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<IInviteEmail> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_INVITEEMAIL_LIST,
  payload: axios.get<IInviteEmail>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IInviteEmail> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_INVITEEMAIL,
    payload: axios.get<IInviteEmail>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IInviteEmail> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_INVITEEMAIL,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IInviteEmail> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_INVITEEMAIL,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IInviteEmail> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_INVITEEMAIL,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
