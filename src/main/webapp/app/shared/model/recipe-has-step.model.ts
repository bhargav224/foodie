import { IRecipe } from 'app/shared/model/recipe.model';
import { IRecipeStep } from 'app/shared/model/recipe-step.model';

export interface IRecipeHasStep {
  id?: number;
  recipe?: IRecipe;
  recipeStep?: IRecipeStep;
}

export const defaultValue: Readonly<IRecipeHasStep> = {};
