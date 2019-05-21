import { IRecipe } from 'app/shared/model/recipe.model';
import { IMeasurement } from 'app/shared/model/measurement.model';
import { IIngredient } from 'app/shared/model/ingredient.model';

export interface IRecipeIngredient {
  id?: number;
  amount?: number;
  ingredientNutritionValue?: number;
  restriction?: string;
  recipe?: IRecipe;
  measurement?: IMeasurement;
  ingredient?: IIngredient;
}

export const defaultValue: Readonly<IRecipeIngredient> = {};
