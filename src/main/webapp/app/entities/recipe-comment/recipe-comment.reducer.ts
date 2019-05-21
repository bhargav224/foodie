import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IRecipeComment, defaultValue } from 'app/shared/model/recipe-comment.model';

export const ACTION_TYPES = {
  SEARCH_RECIPECOMMENTS: 'recipeComment/SEARCH_RECIPECOMMENTS',
  FETCH_RECIPECOMMENT_LIST: 'recipeComment/FETCH_RECIPECOMMENT_LIST',
  FETCH_RECIPECOMMENT: 'recipeComment/FETCH_RECIPECOMMENT',
  CREATE_RECIPECOMMENT: 'recipeComment/CREATE_RECIPECOMMENT',
  UPDATE_RECIPECOMMENT: 'recipeComment/UPDATE_RECIPECOMMENT',
  DELETE_RECIPECOMMENT: 'recipeComment/DELETE_RECIPECOMMENT',
  RESET: 'recipeComment/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IRecipeComment>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type RecipeCommentState = Readonly<typeof initialState>;

// Reducer

export default (state: RecipeCommentState = initialState, action): RecipeCommentState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_RECIPECOMMENTS):
    case REQUEST(ACTION_TYPES.FETCH_RECIPECOMMENT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_RECIPECOMMENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_RECIPECOMMENT):
    case REQUEST(ACTION_TYPES.UPDATE_RECIPECOMMENT):
    case REQUEST(ACTION_TYPES.DELETE_RECIPECOMMENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_RECIPECOMMENTS):
    case FAILURE(ACTION_TYPES.FETCH_RECIPECOMMENT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_RECIPECOMMENT):
    case FAILURE(ACTION_TYPES.CREATE_RECIPECOMMENT):
    case FAILURE(ACTION_TYPES.UPDATE_RECIPECOMMENT):
    case FAILURE(ACTION_TYPES.DELETE_RECIPECOMMENT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_RECIPECOMMENTS):
    case SUCCESS(ACTION_TYPES.FETCH_RECIPECOMMENT_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_RECIPECOMMENT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_RECIPECOMMENT):
    case SUCCESS(ACTION_TYPES.UPDATE_RECIPECOMMENT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_RECIPECOMMENT):
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

const apiUrl = 'api/recipe-comments';
const apiSearchUrl = 'api/_search/recipe-comments';

// Actions

export const getSearchEntities: ICrudSearchAction<IRecipeComment> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_RECIPECOMMENTS,
  payload: axios.get<IRecipeComment>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<IRecipeComment> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_RECIPECOMMENT_LIST,
  payload: axios.get<IRecipeComment>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IRecipeComment> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_RECIPECOMMENT,
    payload: axios.get<IRecipeComment>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IRecipeComment> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_RECIPECOMMENT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IRecipeComment> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_RECIPECOMMENT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IRecipeComment> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_RECIPECOMMENT,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
