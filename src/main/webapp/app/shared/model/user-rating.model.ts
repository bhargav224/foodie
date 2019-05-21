import { Moment } from 'moment';
import { IUserInfo } from 'app/shared/model/user-info.model';

export interface IUserRating {
  id?: number;
  date?: Moment;
  rating?: number;
  rateByUser?: IUserInfo;
  rateToUser?: IUserInfo;
}

export const defaultValue: Readonly<IUserRating> = {};
