import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import UserRating from './user-rating';
import UserRatingDetail from './user-rating-detail';
import UserRatingUpdate from './user-rating-update';
import UserRatingDeleteDialog from './user-rating-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={UserRatingUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={UserRatingUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={UserRatingDetail} />
      <ErrorBoundaryRoute path={match.url} component={UserRating} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={UserRatingDeleteDialog} />
  </>
);

export default Routes;
