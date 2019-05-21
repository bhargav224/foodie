import { IRestaurantMenu } from 'app/shared/model/restaurant-menu.model';
import { IMenuRecipe } from 'app/shared/model/menu-recipe.model';
import { IFoodCategorie } from 'app/shared/model/food-categorie.model';
import { ICourse } from 'app/shared/model/course.model';
import { ICusine } from 'app/shared/model/cusine.model';

export interface IMenuItem {
  id?: number;
  active?: boolean;
  description?: string;
  imagePath?: string;
  name?: string;
  restaurantMenus?: IRestaurantMenu[];
  menuRecipes?: IMenuRecipe[];
  foodCategorie?: IFoodCategorie;
  course?: ICourse;
  cusine?: ICusine;
}

export const defaultValue: Readonly<IMenuItem> = {
  active: false
};
