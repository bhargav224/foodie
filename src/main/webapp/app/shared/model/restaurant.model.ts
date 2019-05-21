import { IRestaurantMenu } from 'app/shared/model/restaurant-menu.model';
import { IAddress } from 'app/shared/model/address.model';
import { IUserInfo } from 'app/shared/model/user-info.model';

export interface IRestaurant {
  id?: number;
  description?: string;
  imagePath?: string;
  name?: string;
  restaurantMenus?: IRestaurantMenu[];
  addresses?: IAddress[];
  userInfos?: IUserInfo[];
}

export const defaultValue: Readonly<IRestaurant> = {};
