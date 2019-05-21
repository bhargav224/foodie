import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IFootnote, defaultValue } from 'app/shared/model/footnote.model';

export const ACTION_TYPES = {
  SEARCH_FOOTNOTES: 'footnote/SEARCH_FOOTNOTES',
  FETCH_FOOTNOTE_LIST: 'footnote/FETCH_FOOTNOTE_LIST',
  FETCH_FOOTNOTE: 'footnote/FETCH_FOOTNOTE',
  CREATE_FOOTNOTE: 'footnote/CREATE_FOOTNOTE',
  UPDATE_FOOTNOTE: 'footnote/UPDATE_FOOTNOTE',
  DELETE_FOOTNOTE: 'footnote/DELETE_FOOTNOTE',
  RESET: 'footnote/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IFootnote>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type FootnoteState = Readonly<typeof initialState>;

// Reducer

export default (state: FootnoteState = initialState, action): FootnoteState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_FOOTNOTES):
    case REQUEST(ACTION_TYPES.FETCH_FOOTNOTE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_FOOTNOTE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_FOOTNOTE):
    case REQUEST(ACTION_TYPES.UPDATE_FOOTNOTE):
    case REQUEST(ACTION_TYPES.DELETE_FOOTNOTE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_FOOTNOTES):
    case FAILURE(ACTION_TYPES.FETCH_FOOTNOTE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_FOOTNOTE):
    case FAILURE(ACTION_TYPES.CREATE_FOOTNOTE):
    case FAILURE(ACTION_TYPES.UPDATE_FOOTNOTE):
    case FAILURE(ACTION_TYPES.DELETE_FOOTNOTE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_FOOTNOTES):
    case SUCCESS(ACTION_TYPES.FETCH_FOOTNOTE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_FOOTNOTE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_FOOTNOTE):
    case SUCCESS(ACTION_TYPES.UPDATE_FOOTNOTE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_FOOTNOTE):
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

const apiUrl = 'api/footnotes';
const apiSearchUrl = 'api/_search/footnotes';

// Actions

export const getSearchEntities: ICrudSearchAction<IFootnote> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_FOOTNOTES,
  payload: axios.get<IFootnote>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<IFootnote> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_FOOTNOTE_LIST,
  payload: axios.get<IFootnote>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IFootnote> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_FOOTNOTE,
    payload: axios.get<IFootnote>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IFootnote> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_FOOTNOTE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IFootnote> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_FOOTNOTE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IFootnote> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_FOOTNOTE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
