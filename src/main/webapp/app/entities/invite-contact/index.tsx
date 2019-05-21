import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import InviteContact from './invite-contact';
import InviteContactDetail from './invite-contact-detail';
import InviteContactUpdate from './invite-contact-update';
import InviteContactDeleteDialog from './invite-contact-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={InviteContactUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={InviteContactUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={InviteContactDetail} />
      <ErrorBoundaryRoute path={match.url} component={InviteContact} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={InviteContactDeleteDialog} />
  </>
);

export default Routes;
