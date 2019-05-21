import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RecipeIngredient from './recipe-ingredient';
import RecipeIngredientDetail from './recipe-ingredient-detail';
import RecipeIngredientUpdate from './recipe-ingredient-update';
import RecipeIngredientDeleteDialog from './recipe-ingredient-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RecipeIngredientUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RecipeIngredientUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RecipeIngredientDetail} />
      <ErrorBoundaryRoute path={match.url} component={RecipeIngredient} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={RecipeIngredientDeleteDialog} />
  </>
);

export default Routes;
