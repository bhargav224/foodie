import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './recipe-has-step.reducer';
import { IRecipeHasStep } from 'app/shared/model/recipe-has-step.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRecipeHasStepDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class RecipeHasStepDetail extends React.Component<IRecipeHasStepDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { recipeHasStepEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            RecipeHasStep [<b>{recipeHasStepEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>Recipe</dt>
            <dd>{recipeHasStepEntity.recipe ? recipeHasStepEntity.recipe.id : ''}</dd>
            <dt>Recipe Step</dt>
            <dd>{recipeHasStepEntity.recipeStep ? recipeHasStepEntity.recipeStep.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/recipe-has-step" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/recipe-has-step/${recipeHasStepEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ recipeHasStep }: IRootState) => ({
  recipeHasStepEntity: recipeHasStep.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RecipeHasStepDetail);
