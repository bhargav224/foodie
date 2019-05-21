import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RecipeRating from './recipe-rating';
import RecipeRatingDetail from './recipe-rating-detail';
import RecipeRatingUpdate from './recipe-rating-update';
import RecipeRatingDeleteDialog from './recipe-rating-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RecipeRatingUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RecipeRatingUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RecipeRatingDetail} />
      <ErrorBoundaryRoute path={match.url} component={RecipeRating} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={RecipeRatingDeleteDialog} />
  </>
);

export default Routes;
