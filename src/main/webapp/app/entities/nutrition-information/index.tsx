import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import NutritionInformation from './nutrition-information';
import NutritionInformationDetail from './nutrition-information-detail';
import NutritionInformationUpdate from './nutrition-information-update';
import NutritionInformationDeleteDialog from './nutrition-information-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={NutritionInformationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={NutritionInformationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={NutritionInformationDetail} />
      <ErrorBoundaryRoute path={match.url} component={NutritionInformation} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={NutritionInformationDeleteDialog} />
  </>
);

export default Routes;
