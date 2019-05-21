import { IRecipe } from 'app/shared/model/recipe.model';
import { IMenuItem } from 'app/shared/model/menu-item.model';

export interface ICusine {
  id?: number;
  active?: boolean;
  description?: string;
  name?: string;
  recipes?: IRecipe[];
  menuItems?: IMenuItem[];
}

export const defaultValue: Readonly<ICusine> = {
  active: false
};
