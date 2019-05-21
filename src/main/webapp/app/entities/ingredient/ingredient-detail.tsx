import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './ingredient.reducer';
import { IIngredient } from 'app/shared/model/ingredient.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IIngredientDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class IngredientDetail extends React.Component<IIngredientDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { ingredientEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Ingredient [<b>{ingredientEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="ingredient">Ingredient</span>
            </dt>
            <dd>{ingredientEntity.ingredient}</dd>
            <dt>
              <span id="active">Active</span>
            </dt>
            <dd>{ingredientEntity.active ? 'true' : 'false'}</dd>
          </dl>
          <Button tag={Link} to="/entity/ingredient" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/ingredient/${ingredientEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ ingredient }: IRootState) => ({
  ingredientEntity: ingredient.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(IngredientDetail);
