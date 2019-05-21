import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Level from './level';
import LevelDetail from './level-detail';
import LevelUpdate from './level-update';
import LevelDeleteDialog from './level-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={LevelUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={LevelUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={LevelDetail} />
      <ErrorBoundaryRoute path={match.url} component={Level} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={LevelDeleteDialog} />
  </>
);

export default Routes;
