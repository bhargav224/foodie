import { IRecipe } from 'app/shared/model/recipe.model';
import { IMenuItem } from 'app/shared/model/menu-item.model';

export interface ICourse {
  id?: number;
  active?: boolean;
  description?: string;
  name?: string;
  recipes?: IRecipe[];
  menuItems?: IMenuItem[];
}

export const defaultValue: Readonly<ICourse> = {
  active: false
};
