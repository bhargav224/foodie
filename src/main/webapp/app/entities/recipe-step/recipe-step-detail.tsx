import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './recipe-step.reducer';
import { IRecipeStep } from 'app/shared/model/recipe-step.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRecipeStepDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class RecipeStepDetail extends React.Component<IRecipeStepDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { recipeStepEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            RecipeStep [<b>{recipeStepEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="instruction">Instruction</span>
            </dt>
            <dd>{recipeStepEntity.instruction}</dd>
            <dt>
              <span id="name">Name</span>
            </dt>
            <dd>{recipeStepEntity.name}</dd>
            <dt>
              <span id="path">Path</span>
            </dt>
            <dd>{recipeStepEntity.path}</dd>
          </dl>
          <Button tag={Link} to="/entity/recipe-step" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/recipe-step/${recipeStepEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ recipeStep }: IRootState) => ({
  recipeStepEntity: recipeStep.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RecipeStepDetail);
