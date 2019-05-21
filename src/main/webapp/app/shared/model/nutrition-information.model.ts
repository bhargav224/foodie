import { IIngredientNutritionInfo } from 'app/shared/model/ingredient-nutrition-info.model';

export interface INutritionInformation {
  id?: number;
  active?: boolean;
  nutrition?: string;
  ingredientNutritionInfos?: IIngredientNutritionInfo[];
}

export const defaultValue: Readonly<INutritionInformation> = {
  active: false
};
