import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { INutritionInformation } from 'app/shared/model/nutrition-information.model';
import { getEntities as getNutritionInformations } from 'app/entities/nutrition-information/nutrition-information.reducer';
import { IIngredient } from 'app/shared/model/ingredient.model';
import { getEntities as getIngredients } from 'app/entities/ingredient/ingredient.reducer';
import { IMeasurement } from 'app/shared/model/measurement.model';
import { getEntities as getMeasurements } from 'app/entities/measurement/measurement.reducer';
import { getEntity, updateEntity, createEntity, reset } from './ingredient-nutrition-info.reducer';
import { IIngredientNutritionInfo } from 'app/shared/model/ingredient-nutrition-info.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IIngredientNutritionInfoUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IIngredientNutritionInfoUpdateState {
  isNew: boolean;
  nutritionInformationId: string;
  ingredientId: string;
  nutritionUnitId: string;
  ingredientUnitId: string;
}

export class IngredientNutritionInfoUpdate extends React.Component<
  IIngredientNutritionInfoUpdateProps,
  IIngredientNutritionInfoUpdateState
> {
  constructor(props) {
    super(props);
    this.state = {
      nutritionInformationId: '0',
      ingredientId: '0',
      nutritionUnitId: '0',
      ingredientUnitId: '0',
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

    this.props.getNutritionInformations();
    this.props.getIngredients();
    this.props.getMeasurements();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { ingredientNutritionInfoEntity } = this.props;
      const entity = {
        ...ingredientNutritionInfoEntity,
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
    this.props.history.push('/entity/ingredient-nutrition-info');
  };

  render() {
    const { ingredientNutritionInfoEntity, nutritionInformations, ingredients, measurements, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="foodieChefApp.ingredientNutritionInfo.home.createOrEditLabel">Create or edit a IngredientNutritionInfo</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : ingredientNutritionInfoEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="ingredient-nutrition-info-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="ingredientAmountLabel" for="ingredientAmount">
                    Ingredient Amount
                  </Label>
                  <AvField
                    id="ingredient-nutrition-info-ingredientAmount"
                    type="string"
                    className="form-control"
                    name="ingredientAmount"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      number: { value: true, errorMessage: 'This field should be a number.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="nutritionAmountLabel" for="nutritionAmount">
                    Nutrition Amount
                  </Label>
                  <AvField
                    id="ingredient-nutrition-info-nutritionAmount"
                    type="string"
                    className="form-control"
                    name="nutritionAmount"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      number: { value: true, errorMessage: 'This field should be a number.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="nutritionInformation.id">Nutrition Information</Label>
                  <AvInput
                    id="ingredient-nutrition-info-nutritionInformation"
                    type="select"
                    className="form-control"
                    name="nutritionInformation.id"
                  >
                    <option value="" key="0" />
                    {nutritionInformations
                      ? nutritionInformations.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="ingredient.id">Ingredient</Label>
                  <AvInput id="ingredient-nutrition-info-ingredient" type="select" className="form-control" name="ingredient.id">
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
                <AvGroup>
                  <Label for="nutritionUnit.id">Nutrition Unit</Label>
                  <AvInput id="ingredient-nutrition-info-nutritionUnit" type="select" className="form-control" name="nutritionUnit.id">
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
                  <Label for="ingredientUnit.id">Ingredient Unit</Label>
                  <AvInput id="ingredient-nutrition-info-ingredientUnit" type="select" className="form-control" name="ingredientUnit.id">
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
                <Button tag={Link} id="cancel-save" to="/entity/ingredient-nutrition-info" replace color="info">
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
  nutritionInformations: storeState.nutritionInformation.entities,
  ingredients: storeState.ingredient.entities,
  measurements: storeState.measurement.entities,
  ingredientNutritionInfoEntity: storeState.ingredientNutritionInfo.entity,
  loading: storeState.ingredientNutritionInfo.loading,
  updating: storeState.ingredientNutritionInfo.updating,
  updateSuccess: storeState.ingredientNutritionInfo.updateSuccess
});

const mapDispatchToProps = {
  getNutritionInformations,
  getIngredients,
  getMeasurements,
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
)(IngredientNutritionInfoUpdate);
