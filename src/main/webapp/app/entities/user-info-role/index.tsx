import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import UserInfoRole from './user-info-role';
import UserInfoRoleDetail from './user-info-role-detail';
import UserInfoRoleUpdate from './user-info-role-update';
import UserInfoRoleDeleteDialog from './user-info-role-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={UserInfoRoleUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={UserInfoRoleUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={UserInfoRoleDetail} />
      <ErrorBoundaryRoute path={match.url} component={UserInfoRole} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={UserInfoRoleDeleteDialog} />
  </>
);

export default Routes;
