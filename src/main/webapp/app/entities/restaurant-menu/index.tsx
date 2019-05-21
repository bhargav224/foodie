import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RestaurantMenu from './restaurant-menu';
import RestaurantMenuDetail from './restaurant-menu-detail';
import RestaurantMenuUpdate from './restaurant-menu-update';
import RestaurantMenuDeleteDialog from './restaurant-menu-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RestaurantMenuUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RestaurantMenuUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RestaurantMenuDetail} />
      <ErrorBoundaryRoute path={match.url} component={RestaurantMenu} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={RestaurantMenuDeleteDialog} />
  </>
);

export default Routes;
