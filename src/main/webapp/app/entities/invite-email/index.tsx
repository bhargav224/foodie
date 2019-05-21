import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import InviteEmail from './invite-email';
import InviteEmailDetail from './invite-email-detail';
import InviteEmailUpdate from './invite-email-update';
import InviteEmailDeleteDialog from './invite-email-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={InviteEmailUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={InviteEmailUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={InviteEmailDetail} />
      <ErrorBoundaryRoute path={match.url} component={InviteEmail} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={InviteEmailDeleteDialog} />
  </>
);

export default Routes;
