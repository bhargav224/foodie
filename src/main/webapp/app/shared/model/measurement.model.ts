import { IRecipeIngredient } from 'app/shared/model/recipe-ingredient.model';
import { IIngredientNutritionInfo } from 'app/shared/model/ingredient-nutrition-info.model';

export interface IMeasurement {
  id?: number;
  abbreviation?: string;
  active?: boolean;
  recipeIngredients?: IRecipeIngredient[];
  forNutritionUnits?: IIngredientNutritionInfo[];
  forIngredientUnits?: IIngredientNutritionInfo[];
}

export const defaultValue: Readonly<IMeasurement> = {
  active: false
};
