import { IRecipe } from 'app/shared/model/recipe.model';

export interface IRecipeImage {
  id?: number;
  imagePath?: string;
  recipe?: IRecipe;
}

export const defaultValue: Readonly<IRecipeImage> = {};
