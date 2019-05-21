import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './recipe-ingredient.reducer';
import { IRecipeIngredient } from 'app/shared/model/recipe-ingredient.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRecipeIngredientDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class RecipeIngredientDetail extends React.Component<IRecipeIngredientDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { recipeIngredientEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            RecipeIngredient [<b>{recipeIngredientEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="amount">Amount</span>
            </dt>
            <dd>{recipeIngredientEntity.amount}</dd>
            <dt>
              <span id="ingredientNutritionValue">Ingredient Nutrition Value</span>
            </dt>
            <dd>{recipeIngredientEntity.ingredientNutritionValue}</dd>
            <dt>
              <span id="restriction">Restriction</span>
            </dt>
            <dd>{recipeIngredientEntity.restriction}</dd>
            <dt>Recipe</dt>
            <dd>{recipeIngredientEntity.recipe ? recipeIngredientEntity.recipe.id : ''}</dd>
            <dt>Measurement</dt>
            <dd>{recipeIngredientEntity.measurement ? recipeIngredientEntity.measurement.id : ''}</dd>
            <dt>Ingredient</dt>
            <dd>{recipeIngredientEntity.ingredient ? recipeIngredientEntity.ingredient.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/recipe-ingredient" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/recipe-ingredient/${recipeIngredientEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ recipeIngredient }: IRootState) => ({
  recipeIngredientEntity: recipeIngredient.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RecipeIngredientDetail);
