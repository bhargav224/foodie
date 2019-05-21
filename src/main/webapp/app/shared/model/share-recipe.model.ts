import { Moment } from 'moment';
import { IRecipe } from 'app/shared/model/recipe.model';
import { IUserInfo } from 'app/shared/model/user-info.model';

export interface IShareRecipe {
  id?: number;
  date?: Moment;
  recipe?: IRecipe;
  sharedBy?: IUserInfo;
  sharedTo?: IUserInfo;
}

export const defaultValue: Readonly<IShareRecipe> = {};
