import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RecipeImage from './recipe-image';
import RecipeImageDetail from './recipe-image-detail';
import RecipeImageUpdate from './recipe-image-update';
import RecipeImageDeleteDialog from './recipe-image-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RecipeImageUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RecipeImageUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RecipeImageDetail} />
      <ErrorBoundaryRoute path={match.url} component={RecipeImage} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={RecipeImageDeleteDialog} />
  </>
);

export default Routes;
