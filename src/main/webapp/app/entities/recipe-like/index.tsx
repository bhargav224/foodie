import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RecipeLike from './recipe-like';
import RecipeLikeDetail from './recipe-like-detail';
import RecipeLikeUpdate from './recipe-like-update';
import RecipeLikeDeleteDialog from './recipe-like-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RecipeLikeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RecipeLikeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RecipeLikeDetail} />
      <ErrorBoundaryRoute path={match.url} component={RecipeLike} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={RecipeLikeDeleteDialog} />
  </>
);

export default Routes;
