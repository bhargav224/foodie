import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from 'app/modules/administration/user-management/user-management.reducer';
import register, { RegisterState } from 'app/modules/account/register/register.reducer';
import activate, { ActivateState } from 'app/modules/account/activate/activate.reducer';
import password, { PasswordState } from 'app/modules/account/password/password.reducer';
import settings, { SettingsState } from 'app/modules/account/settings/settings.reducer';
import passwordReset, { PasswordResetState } from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import recipe, {
  RecipeState
} from 'app/entities/recipe/recipe.reducer';
// prettier-ignore
import recipeStep, {
  RecipeStepState
} from 'app/entities/recipe-step/recipe-step.reducer';
// prettier-ignore
import recipeHasStep, {
  RecipeHasStepState
} from 'app/entities/recipe-has-step/recipe-has-step.reducer';
// prettier-ignore
import recipeImage, {
  RecipeImageState
} from 'app/entities/recipe-image/recipe-image.reducer';
// prettier-ignore
import level, {
  LevelState
} from 'app/entities/level/level.reducer';
// prettier-ignore
import foodCategorie, {
  FoodCategorieState
} from 'app/entities/food-categorie/food-categorie.reducer';
// prettier-ignore
import course, {
  CourseState
} from 'app/entities/course/course.reducer';
// prettier-ignore
import cusine, {
  CusineState
} from 'app/entities/cusine/cusine.reducer';
// prettier-ignore
import menuItem, {
  MenuItemState
} from 'app/entities/menu-item/menu-item.reducer';
// prettier-ignore
import restaurant, {
  RestaurantState
} from 'app/entities/restaurant/restaurant.reducer';
// prettier-ignore
import address, {
  AddressState
} from 'app/entities/address/address.reducer';
// prettier-ignore
import restaurantMenu, {
  RestaurantMenuState
} from 'app/entities/restaurant-menu/restaurant-menu.reducer';
// prettier-ignore
import menuRecipe, {
  MenuRecipeState
} from 'app/entities/menu-recipe/menu-recipe.reducer';
// prettier-ignore
import userInfo, {
  UserInfoState
} from 'app/entities/user-info/user-info.reducer';
// prettier-ignore
import recipeComment, {
  RecipeCommentState
} from 'app/entities/recipe-comment/recipe-comment.reducer';
// prettier-ignore
import recipeRating, {
  RecipeRatingState
} from 'app/entities/recipe-rating/recipe-rating.reducer';
// prettier-ignore
import shareRecipe, {
  ShareRecipeState
} from 'app/entities/share-recipe/share-recipe.reducer';
// prettier-ignore
import recipeLike, {
  RecipeLikeState
} from 'app/entities/recipe-like/recipe-like.reducer';
// prettier-ignore
import footnote, {
  FootnoteState
} from 'app/entities/footnote/footnote.reducer';
// prettier-ignore
import inviteEmail, {
  InviteEmailState
} from 'app/entities/invite-email/invite-email.reducer';
// prettier-ignore
import inviteContact, {
  InviteContactState
} from 'app/entities/invite-contact/invite-contact.reducer';
// prettier-ignore
import userRating, {
  UserRatingState
} from 'app/entities/user-rating/user-rating.reducer';
// prettier-ignore
import chefProfile, {
  ChefProfileState
} from 'app/entities/chef-profile/chef-profile.reducer';
// prettier-ignore
import nutritionInformation, {
  NutritionInformationState
} from 'app/entities/nutrition-information/nutrition-information.reducer';
// prettier-ignore
import ingredientNutritionInfo, {
  IngredientNutritionInfoState
} from 'app/entities/ingredient-nutrition-info/ingredient-nutrition-info.reducer';
// prettier-ignore
import measurement, {
  MeasurementState
} from 'app/entities/measurement/measurement.reducer';
// prettier-ignore
import ingredient, {
  IngredientState
} from 'app/entities/ingredient/ingredient.reducer';
// prettier-ignore
import recipeIngredient, {
  RecipeIngredientState
} from 'app/entities/recipe-ingredient/recipe-ingredient.reducer';
// prettier-ignore
import role, {
  RoleState
} from 'app/entities/role/role.reducer';
// prettier-ignore
import userInfoRole, {
  UserInfoRoleState
} from 'app/entities/user-info-role/user-info-role.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly userManagement: UserManagementState;
  readonly register: RegisterState;
  readonly activate: ActivateState;
  readonly passwordReset: PasswordResetState;
  readonly password: PasswordState;
  readonly settings: SettingsState;
  readonly recipe: RecipeState;
  readonly recipeStep: RecipeStepState;
  readonly recipeHasStep: RecipeHasStepState;
  readonly recipeImage: RecipeImageState;
  readonly level: LevelState;
  readonly foodCategorie: FoodCategorieState;
  readonly course: CourseState;
  readonly cusine: CusineState;
  readonly menuItem: MenuItemState;
  readonly restaurant: RestaurantState;
  readonly address: AddressState;
  readonly restaurantMenu: RestaurantMenuState;
  readonly menuRecipe: MenuRecipeState;
  readonly userInfo: UserInfoState;
  readonly recipeComment: RecipeCommentState;
  readonly recipeRating: RecipeRatingState;
  readonly shareRecipe: ShareRecipeState;
  readonly recipeLike: RecipeLikeState;
  readonly footnote: FootnoteState;
  readonly inviteEmail: InviteEmailState;
  readonly inviteContact: InviteContactState;
  readonly userRating: UserRatingState;
  readonly chefProfile: ChefProfileState;
  readonly nutritionInformation: NutritionInformationState;
  readonly ingredientNutritionInfo: IngredientNutritionInfoState;
  readonly measurement: MeasurementState;
  readonly ingredient: IngredientState;
  readonly recipeIngredient: RecipeIngredientState;
  readonly role: RoleState;
  readonly userInfoRole: UserInfoRoleState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  recipe,
  recipeStep,
  recipeHasStep,
  recipeImage,
  level,
  foodCategorie,
  course,
  cusine,
  menuItem,
  restaurant,
  address,
  restaurantMenu,
  menuRecipe,
  userInfo,
  recipeComment,
  recipeRating,
  shareRecipe,
  recipeLike,
  footnote,
  inviteEmail,
  inviteContact,
  userRating,
  chefProfile,
  nutritionInformation,
  ingredientNutritionInfo,
  measurement,
  ingredient,
  recipeIngredient,
  role,
  userInfoRole,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar
});

export default rootReducer;
