import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ChefProfile from './chef-profile';
import ChefProfileDetail from './chef-profile-detail';
import ChefProfileUpdate from './chef-profile-update';
import ChefProfileDeleteDialog from './chef-profile-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ChefProfileUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ChefProfileUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ChefProfileDetail} />
      <ErrorBoundaryRoute path={match.url} component={ChefProfile} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ChefProfileDeleteDialog} />
  </>
);

export default Routes;
