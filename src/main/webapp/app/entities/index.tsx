import React from 'react';
import { Switch } from 'react-router-dom';

// tslint:disable-next-line:no-unused-variable
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Recipe from './recipe';
import RecipeStep from './recipe-step';
import RecipeHasStep from './recipe-has-step';
import RecipeImage from './recipe-image';
import Level from './level';
import FoodCategorie from './food-categorie';
import Course from './course';
import Cusine from './cusine';
import MenuItem from './menu-item';
import Restaurant from './restaurant';
import Address from './address';
import RestaurantMenu from './restaurant-menu';
import MenuRecipe from './menu-recipe';
import UserInfo from './user-info';
import RecipeComment from './recipe-comment';
import RecipeRating from './recipe-rating';
import ShareRecipe from './share-recipe';
import RecipeLike from './recipe-like';
import Footnote from './footnote';
import InviteEmail from './invite-email';
import InviteContact from './invite-contact';
import UserRating from './user-rating';
import ChefProfile from './chef-profile';
import NutritionInformation from './nutrition-information';
import IngredientNutritionInfo from './ingredient-nutrition-info';
import Measurement from './measurement';
import Ingredient from './ingredient';
import RecipeIngredient from './recipe-ingredient';
import Role from './role';
import UserInfoRole from './user-info-role';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}/recipe`} component={Recipe} />
      <ErrorBoundaryRoute path={`${match.url}/recipe-step`} component={RecipeStep} />
      <ErrorBoundaryRoute path={`${match.url}/recipe-has-step`} component={RecipeHasStep} />
      <ErrorBoundaryRoute path={`${match.url}/recipe-image`} component={RecipeImage} />
      <ErrorBoundaryRoute path={`${match.url}/level`} component={Level} />
      <ErrorBoundaryRoute path={`${match.url}/food-categorie`} component={FoodCategorie} />
      <ErrorBoundaryRoute path={`${match.url}/course`} component={Course} />
      <ErrorBoundaryRoute path={`${match.url}/cusine`} component={Cusine} />
      <ErrorBoundaryRoute path={`${match.url}/menu-item`} component={MenuItem} />
      <ErrorBoundaryRoute path={`${match.url}/restaurant`} component={Restaurant} />
      <ErrorBoundaryRoute path={`${match.url}/address`} component={Address} />
      <ErrorBoundaryRoute path={`${match.url}/restaurant-menu`} component={RestaurantMenu} />
      <ErrorBoundaryRoute path={`${match.url}/menu-recipe`} component={MenuRecipe} />
      <ErrorBoundaryRoute path={`${match.url}/user-info`} component={UserInfo} />
      <ErrorBoundaryRoute path={`${match.url}/recipe-comment`} component={RecipeComment} />
      <ErrorBoundaryRoute path={`${match.url}/recipe-rating`} component={RecipeRating} />
      <ErrorBoundaryRoute path={`${match.url}/share-recipe`} component={ShareRecipe} />
      <ErrorBoundaryRoute path={`${match.url}/recipe-like`} component={RecipeLike} />
      <ErrorBoundaryRoute path={`${match.url}/footnote`} component={Footnote} />
      <ErrorBoundaryRoute path={`${match.url}/invite-email`} component={InviteEmail} />
      <ErrorBoundaryRoute path={`${match.url}/invite-contact`} component={InviteContact} />
      <ErrorBoundaryRoute path={`${match.url}/user-rating`} component={UserRating} />
      <ErrorBoundaryRoute path={`${match.url}/chef-profile`} component={ChefProfile} />
      <ErrorBoundaryRoute path={`${match.url}/nutrition-information`} component={NutritionInformation} />
      <ErrorBoundaryRoute path={`${match.url}/ingredient-nutrition-info`} component={IngredientNutritionInfo} />
      <ErrorBoundaryRoute path={`${match.url}/measurement`} component={Measurement} />
      <ErrorBoundaryRoute path={`${match.url}/ingredient`} component={Ingredient} />
      <ErrorBoundaryRoute path={`${match.url}/recipe-ingredient`} component={RecipeIngredient} />
      <ErrorBoundaryRoute path={`${match.url}/role`} component={Role} />
      <ErrorBoundaryRoute path={`${match.url}/user-info-role`} component={UserInfoRole} />
      {/* jhipster-needle-add-route-path - JHipster will routes here */}
    </Switch>
  </div>
);

export default Routes;
