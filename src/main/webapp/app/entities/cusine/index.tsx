import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Cusine from './cusine';
import CusineDetail from './cusine-detail';
import CusineUpdate from './cusine-update';
import CusineDeleteDialog from './cusine-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CusineUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CusineUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CusineDetail} />
      <ErrorBoundaryRoute path={match.url} component={Cusine} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={CusineDeleteDialog} />
  </>
);

export default Routes;
