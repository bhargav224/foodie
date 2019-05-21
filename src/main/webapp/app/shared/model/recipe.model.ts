import { IRecipeHasStep } from 'app/shared/model/recipe-has-step.model';
import { IRecipeImage } from 'app/shared/model/recipe-image.model';
import { IMenuRecipe } from 'app/shared/model/menu-recipe.model';
import { IRecipeComment } from 'app/shared/model/recipe-comment.model';
import { IRecipeRating } from 'app/shared/model/recipe-rating.model';
import { IShareRecipe } from 'app/shared/model/share-recipe.model';
import { IRecipeLike } from 'app/shared/model/recipe-like.model';
import { IFootnote } from 'app/shared/model/footnote.model';
import { IRecipeIngredient } from 'app/shared/model/recipe-ingredient.model';
import { ILevel } from 'app/shared/model/level.model';
import { IFoodCategorie } from 'app/shared/model/food-categorie.model';
import { ICusine } from 'app/shared/model/cusine.model';
import { ICourse } from 'app/shared/model/course.model';

export interface IRecipe {
  id?: number;
  amount?: number;
  caloriesPerServings?: number;
  cookTime?: number;
  description?: string;
  prepTime?: number;
  published?: boolean;
  rating?: number;
  readyIn?: number;
  title?: string;
  video?: string;
  yields?: number;
  recipeHasSteps?: IRecipeHasStep[];
  recipeImages?: IRecipeImage[];
  menuRecipes?: IMenuRecipe[];
  recipeComments?: IRecipeComment[];
  recipeRatings?: IRecipeRating[];
  shareRecipes?: IShareRecipe[];
  recipeLikes?: IRecipeLike[];
  footnotes?: IFootnote[];
  recipeIngredients?: IRecipeIngredient[];
  level?: ILevel;
  foodCategorie?: IFoodCategorie;
  cusine?: ICusine;
  course?: ICourse;
}

export const defaultValue: Readonly<IRecipe> = {
  published: false
};
