import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RecipeComment from './recipe-comment';
import RecipeCommentDetail from './recipe-comment-detail';
import RecipeCommentUpdate from './recipe-comment-update';
import RecipeCommentDeleteDialog from './recipe-comment-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RecipeCommentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RecipeCommentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RecipeCommentDetail} />
      <ErrorBoundaryRoute path={match.url} component={RecipeComment} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={RecipeCommentDeleteDialog} />
  </>
);

export default Routes;
