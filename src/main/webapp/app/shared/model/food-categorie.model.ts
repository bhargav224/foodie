import { IRecipe } from 'app/shared/model/recipe.model';
import { IMenuItem } from 'app/shared/model/menu-item.model';

export interface IFoodCategorie {
  id?: number;
  active?: boolean;
  category?: string;
  recipes?: IRecipe[];
  menuItems?: IMenuItem[];
}

export const defaultValue: Readonly<IFoodCategorie> = {
  active: false
};
