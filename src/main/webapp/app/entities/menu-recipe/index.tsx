import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import MenuRecipe from './menu-recipe';
import MenuRecipeDetail from './menu-recipe-detail';
import MenuRecipeUpdate from './menu-recipe-update';
import MenuRecipeDeleteDialog from './menu-recipe-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={MenuRecipeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={MenuRecipeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={MenuRecipeDetail} />
      <ErrorBoundaryRoute path={match.url} component={MenuRecipe} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={MenuRecipeDeleteDialog} />
  </>
);

export default Routes;
