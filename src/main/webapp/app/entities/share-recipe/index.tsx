import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ShareRecipe from './share-recipe';
import ShareRecipeDetail from './share-recipe-detail';
import ShareRecipeUpdate from './share-recipe-update';
import ShareRecipeDeleteDialog from './share-recipe-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ShareRecipeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ShareRecipeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ShareRecipeDetail} />
      <ErrorBoundaryRoute path={match.url} component={ShareRecipe} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ShareRecipeDeleteDialog} />
  </>
);

export default Routes;
