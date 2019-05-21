import { INutritionInformation } from 'app/shared/model/nutrition-information.model';
import { IIngredient } from 'app/shared/model/ingredient.model';
import { IMeasurement } from 'app/shared/model/measurement.model';

export interface IIngredientNutritionInfo {
  id?: number;
  ingredientAmount?: number;
  nutritionAmount?: number;
  nutritionInformation?: INutritionInformation;
  ingredient?: IIngredient;
  nutritionUnit?: IMeasurement;
  ingredientUnit?: IMeasurement;
}

export const defaultValue: Readonly<IIngredientNutritionInfo> = {};
