import { Moment } from 'moment';
import { IRecipe } from 'app/shared/model/recipe.model';
import { IUserInfo } from 'app/shared/model/user-info.model';

export interface IRecipeComment {
  id?: number;
  comment?: string;
  date?: Moment;
  recipe?: IRecipe;
  userInfo?: IUserInfo;
}

export const defaultValue: Readonly<IRecipeComment> = {};
