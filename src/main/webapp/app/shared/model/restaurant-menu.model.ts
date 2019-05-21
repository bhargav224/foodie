import { IMenuItem } from 'app/shared/model/menu-item.model';
import { IRestaurant } from 'app/shared/model/restaurant.model';

export interface IRestaurantMenu {
  id?: number;
  menuItem?: IMenuItem;
  restaurant?: IRestaurant;
}

export const defaultValue: Readonly<IRestaurantMenu> = {};
