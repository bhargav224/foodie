import { IRecipe } from 'app/shared/model/recipe.model';

export interface ILevel {
  id?: number;
  active?: boolean;
  description?: string;
  recipes?: IRecipe[];
}

export const defaultValue: Readonly<ILevel> = {
  active: false
};
