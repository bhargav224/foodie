import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IRecipe } from 'app/shared/model/recipe.model';
import { getEntities as getRecipes } from 'app/entities/recipe/recipe.reducer';
import { IRecipeStep } from 'app/shared/model/recipe-step.model';
import { getEntities as getRecipeSteps } from 'app/entities/recipe-step/recipe-step.reducer';
import { getEntity, updateEntity, createEntity, reset } from './recipe-has-step.reducer';
import { IRecipeHasStep } from 'app/shared/model/recipe-has-step.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IRecipeHasStepUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IRecipeHasStepUpdateState {
  isNew: boolean;
  recipeId: string;
  recipeStepId: string;
}

export class RecipeHasStepUpdate extends React.Component<IRecipeHasStepUpdateProps, IRecipeHasStepUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      recipeId: '0',
      recipeStepId: '0',
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
    this.props.getRecipeSteps();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { recipeHasStepEntity } = this.props;
      const entity = {
        ...recipeHasStepEntity,
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
    this.props.history.push('/entity/recipe-has-step');
  };

  render() {
    const { recipeHasStepEntity, recipes, recipeSteps, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="foodieChefApp.recipeHasStep.home.createOrEditLabel">Create or edit a RecipeHasStep</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : recipeHasStepEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="recipe-has-step-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label for="recipe.id">Recipe</Label>
                  <AvInput id="recipe-has-step-recipe" type="select" className="form-control" name="recipe.id">
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
                  <Label for="recipeStep.id">Recipe Step</Label>
                  <AvInput id="recipe-has-step-recipeStep" type="select" className="form-control" name="recipeStep.id">
                    <option value="" key="0" />
                    {recipeSteps
                      ? recipeSteps.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/recipe-has-step" replace color="info">
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
  recipeSteps: storeState.recipeStep.entities,
  recipeHasStepEntity: storeState.recipeHasStep.entity,
  loading: storeState.recipeHasStep.loading,
  updating: storeState.recipeHasStep.updating,
  updateSuccess: storeState.recipeHasStep.updateSuccess
});

const mapDispatchToProps = {
  getRecipes,
  getRecipeSteps,
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
)(RecipeHasStepUpdate);
