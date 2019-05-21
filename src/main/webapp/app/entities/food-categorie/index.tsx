import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import FoodCategorie from './food-categorie';
import FoodCategorieDetail from './food-categorie-detail';
import FoodCategorieUpdate from './food-categorie-update';
import FoodCategorieDeleteDialog from './food-categorie-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FoodCategorieUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FoodCategorieUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FoodCategorieDetail} />
      <ErrorBoundaryRoute path={match.url} component={FoodCategorie} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={FoodCategorieDeleteDialog} />
  </>
);

export default Routes;
