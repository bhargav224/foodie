import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Footnote from './footnote';
import FootnoteDetail from './footnote-detail';
import FootnoteUpdate from './footnote-update';
import FootnoteDeleteDialog from './footnote-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FootnoteUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FootnoteUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FootnoteDetail} />
      <ErrorBoundaryRoute path={match.url} component={Footnote} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={FootnoteDeleteDialog} />
  </>
);

export default Routes;
