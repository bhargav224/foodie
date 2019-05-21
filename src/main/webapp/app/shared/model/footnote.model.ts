import { IRecipe } from 'app/shared/model/recipe.model';
import { IUserInfo } from 'app/shared/model/user-info.model';

export interface IFootnote {
  id?: number;
  footnote?: string;
  recipe?: IRecipe;
  userInfo?: IUserInfo;
}

export const defaultValue: Readonly<IFootnote> = {};
