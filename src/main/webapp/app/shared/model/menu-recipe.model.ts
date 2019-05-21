import { IRecipe } from 'app/shared/model/recipe.model';
import { IMenuItem } from 'app/shared/model/menu-item.model';

export interface IMenuRecipe {
  id?: number;
  recipe?: IRecipe;
  menuItem?: IMenuItem;
}

export const defaultValue: Readonly<IMenuRecipe> = {};
