import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IRecipe } from 'app/shared/model/recipe.model';
import { getEntities as getRecipes } from 'app/entities/recipe/recipe.reducer';
import { IMeasurement } from 'app/shared/model/measurement.model';
import { getEntities as getMeasurements } from 'app/entities/measurement/measurement.reducer';
import { IIngredient } from 'app/shared/model/ingredient.model';
import { getEntities as getIngredients } from 'app/entities/ingredient/ingredient.reducer';
import { getEntity, updateEntity, createEntity, reset } from './recipe-ingredient.reducer';
import { IRecipeIngredient } from 'app/shared/model/recipe-ingredient.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IRecipeIngredientUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IRecipeIngredientUpdateState {
  isNew: boolean;
  recipeId: string;
  measurementId: string;
  ingredientId: string;
}

export class RecipeIngredientUpdate extends React.Component<IRecipeIngredientUpdateProps, IRecipeIngredientUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      recipeId: '0',
      measurementId: '0',
      ingredientId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getRecipes();
    this.props.getMeasurements();
    this.props.getIngredients();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { recipeIngredientEntity } = this.props;
      const entity = {
        ...recipeIngredientEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/recipe-ingredient');
  };

  render() {
    const { recipeIngredientEntity, recipes, measurements, ingredients, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="foodieChefApp.recipeIngredient.home.createOrEditLabel">Create or edit a RecipeIngredient</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : recipeIngredientEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="recipe-ingredient-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="amountLabel" for="amount">
                    Amount
                  </Label>
                  <AvField
                    id="recipe-ingredient-amount"
                    type="string"
                    className="form-control"
                    name="amount"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      number: { value: true, errorMessage: 'This field should be a number.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="ingredientNutritionValueLabel" for="ingredientNutritionValue">
                    Ingredient Nutrition Value
                  </Label>
                  <AvField
                    id="recipe-ingredient-ingredientNutritionValue"
                    type="string"
                    className="form-control"
                    name="ingredientNutritionValue"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      number: { value: true, errorMessage: 'This field should be a number.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="restrictionLabel" for="restriction">
                    Restriction
                  </Label>
                  <AvField
                    id="recipe-ingredient-restriction"
                    type="text"
                    name="restriction"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="recipe.id">Recipe</Label>
                  <AvInput id="recipe-ingredient-recipe" type="select" className="form-control" name="recipe.id">
                    <option value="" key="0" />
                    {recipes
                      ? recipes.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="measurement.id">Measurement</Label>
                  <AvInput id="recipe-ingredient-measurement" type="select" className="form-control" name="measurement.id">
                    <option value="" key="0" />
                    {measurements
                      ? measurements.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="ingredient.id">Ingredient</Label>
                  <AvInput id="recipe-ingredient-ingredient" type="select" className="form-control" name="ingredient.id">
                    <option value="" key="0" />
                    {ingredients
                      ? ingredients.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/recipe-ingredient" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">Back</span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp; Save
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  recipes: storeState.recipe.entities,
  measurements: storeState.measurement.entities,
  ingredients: storeState.ingredient.entities,
  recipeIngredientEntity: storeState.recipeIngredient.entity,
  loading: storeState.recipeIngredient.loading,
  updating: storeState.recipeIngredient.updating,
  updateSuccess: storeState.recipeIngredient.updateSuccess
});

const mapDispatchToProps = {
  getRecipes,
  getMeasurements,
  getIngredients,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RecipeIngredientUpdate);
