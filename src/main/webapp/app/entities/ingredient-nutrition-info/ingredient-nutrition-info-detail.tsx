import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './ingredient-nutrition-info.reducer';
import { IIngredientNutritionInfo } from 'app/shared/model/ingredient-nutrition-info.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IIngredientNutritionInfoDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class IngredientNutritionInfoDetail extends React.Component<IIngredientNutritionInfoDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { ingredientNutritionInfoEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            IngredientNutritionInfo [<b>{ingredientNutritionInfoEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="ingredientAmount">Ingredient Amount</span>
            </dt>
            <dd>{ingredientNutritionInfoEntity.ingredientAmount}</dd>
            <dt>
              <span id="nutritionAmount">Nutrition Amount</span>
            </dt>
            <dd>{ingredientNutritionInfoEntity.nutritionAmount}</dd>
            <dt>Nutrition Information</dt>
            <dd>{ingredientNutritionInfoEntity.nutritionInformation ? ingredientNutritionInfoEntity.nutritionInformation.id : ''}</dd>
            <dt>Ingredient</dt>
            <dd>{ingredientNutritionInfoEntity.ingredient ? ingredientNutritionInfoEntity.ingredient.id : ''}</dd>
            <dt>Nutrition Unit</dt>
            <dd>{ingredientNutritionInfoEntity.nutritionUnit ? ingredientNutritionInfoEntity.nutritionUnit.id : ''}</dd>
            <dt>Ingredient Unit</dt>
            <dd>{ingredientNutritionInfoEntity.ingredientUnit ? ingredientNutritionInfoEntity.ingredientUnit.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/ingredient-nutrition-info" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/ingredient-nutrition-info/${ingredientNutritionInfoEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ ingredientNutritionInfo }: IRootState) => ({
  ingredientNutritionInfoEntity: ingredientNutritionInfo.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(IngredientNutritionInfoDetail);
