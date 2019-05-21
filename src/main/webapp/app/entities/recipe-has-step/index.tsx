import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RecipeHasStep from './recipe-has-step';
import RecipeHasStepDetail from './recipe-has-step-detail';
import RecipeHasStepUpdate from './recipe-has-step-update';
import RecipeHasStepDeleteDialog from './recipe-has-step-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RecipeHasStepUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RecipeHasStepUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RecipeHasStepDetail} />
      <ErrorBoundaryRoute path={match.url} component={RecipeHasStep} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={RecipeHasStepDeleteDialog} />
  </>
);

export default Routes;
