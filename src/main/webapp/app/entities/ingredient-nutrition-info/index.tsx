import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import IngredientNutritionInfo from './ingredient-nutrition-info';
import IngredientNutritionInfoDetail from './ingredient-nutrition-info-detail';
import IngredientNutritionInfoUpdate from './ingredient-nutrition-info-update';
import IngredientNutritionInfoDeleteDialog from './ingredient-nutrition-info-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={IngredientNutritionInfoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={IngredientNutritionInfoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={IngredientNutritionInfoDetail} />
      <ErrorBoundaryRoute path={match.url} component={IngredientNutritionInfo} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={IngredientNutritionInfoDeleteDialog} />
  </>
);

export default Routes;
