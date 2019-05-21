import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RecipeStep from './recipe-step';
import RecipeStepDetail from './recipe-step-detail';
import RecipeStepUpdate from './recipe-step-update';
import RecipeStepDeleteDialog from './recipe-step-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RecipeStepUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RecipeStepUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RecipeStepDetail} />
      <ErrorBoundaryRoute path={match.url} component={RecipeStep} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={RecipeStepDeleteDialog} />
  </>
);

export default Routes;
