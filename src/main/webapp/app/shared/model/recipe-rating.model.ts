import { Moment } from 'moment';
import { IRecipe } from 'app/shared/model/recipe.model';
import { IUserInfo } from 'app/shared/model/user-info.model';

export interface IRecipeRating {
  id?: number;
  date?: Moment;
  rating?: number;
  recipe?: IRecipe;
  userInfo?: IUserInfo;
}

export const defaultValue: Readonly<IRecipeRating> = {};
