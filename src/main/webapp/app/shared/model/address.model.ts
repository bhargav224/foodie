import { IRestaurant } from 'app/shared/model/restaurant.model';

export interface IAddress {
  id?: number;
  city?: string;
  country?: string;
  state?: string;
  streetName?: string;
  zipCode?: number;
  restaurant?: IRestaurant;
}

export const defaultValue: Readonly<IAddress> = {};
