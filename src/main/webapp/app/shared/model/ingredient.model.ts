import { IRecipeIngredient } from 'app/shared/model/recipe-ingredient.model';
import { IIngredientNutritionInfo } from 'app/shared/model/ingredient-nutrition-info.model';

export interface IIngredient {
  id?: number;
  ingredient?: string;
  active?: boolean;
  recipeIngredients?: IRecipeIngredient[];
  ingredientNutritionInfos?: IIngredientNutritionInfo[];
}

export const defaultValue: Readonly<IIngredient> = {
  active: false
};
