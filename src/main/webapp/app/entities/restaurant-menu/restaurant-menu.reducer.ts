import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IRestaurantMenu, defaultValue } from 'app/shared/model/restaurant-menu.model';

export const ACTION_TYPES = {
  SEARCH_RESTAURANTMENUS: 'restaurantMenu/SEARCH_RESTAURANTMENUS',
  FETCH_RESTAURANTMENU_LIST: 'restaurantMenu/FETCH_RESTAURANTMENU_LIST',
  FETCH_RESTAURANTMENU: 'restaurantMenu/FETCH_RESTAURANTMENU',
  CREATE_RESTAURANTMENU: 'restaurantMenu/CREATE_RESTAURANTMENU',
  UPDATE_RESTAURANTMENU: 'restaurantMenu/UPDATE_RESTAURANTMENU',
  DELETE_RESTAURANTMENU: 'restaurantMenu/DELETE_RESTAURANTMENU',
  RESET: 'restaurantMenu/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IRestaurantMenu>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type RestaurantMenuState = Readonly<typeof initialState>;

// Reducer

export default (state: RestaurantMenuState = initialState, action): RestaurantMenuState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_RESTAURANTMENUS):
    case REQUEST(ACTION_TYPES.FETCH_RESTAURANTMENU_LIST):
    case REQUEST(ACTION_TYPES.FETCH_RESTAURANTMENU):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_RESTAURANTMENU):
    case REQUEST(ACTION_TYPES.UPDATE_RESTAURANTMENU):
    case REQUEST(ACTION_TYPES.DELETE_RESTAURANTMENU):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_RESTAURANTMENUS):
    case FAILURE(ACTION_TYPES.FETCH_RESTAURANTMENU_LIST):
    case FAILURE(ACTION_TYPES.FETCH_RESTAURANTMENU):
    case FAILURE(ACTION_TYPES.CREATE_RESTAURANTMENU):
    case FAILURE(ACTION_TYPES.UPDATE_RESTAURANTMENU):
    case FAILURE(ACTION_TYPES.DELETE_RESTAURANTMENU):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_RESTAURANTMENUS):
    case SUCCESS(ACTION_TYPES.FETCH_RESTAURANTMENU_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_RESTAURANTMENU):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_RESTAURANTMENU):
    case SUCCESS(ACTION_TYPES.UPDATE_RESTAURANTMENU):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_RESTAURANTMENU):
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

const apiUrl = 'api/restaurant-menus';
const apiSearchUrl = 'api/_search/restaurant-menus';

// Actions

export const getSearchEntities: ICrudSearchAction<IRestaurantMenu> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_RESTAURANTMENUS,
  payload: axios.get<IRestaurantMenu>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<IRestaurantMenu> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_RESTAURANTMENU_LIST,
  payload: axios.get<IRestaurantMenu>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IRestaurantMenu> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_RESTAURANTMENU,
    payload: axios.get<IRestaurantMenu>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IRestaurantMenu> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_RESTAURANTMENU,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IRestaurantMenu> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_RESTAURANTMENU,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IRestaurantMenu> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_RESTAURANTMENU,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
