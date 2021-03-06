import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './restaurant-menu.reducer';
import { IRestaurantMenu } from 'app/shared/model/restaurant-menu.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRestaurantMenuDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class RestaurantMenuDetail extends React.Component<IRestaurantMenuDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { restaurantMenuEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            RestaurantMenu [<b>{restaurantMenuEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>Menu Item</dt>
            <dd>{restaurantMenuEntity.menuItem ? restaurantMenuEntity.menuItem.id : ''}</dd>
            <dt>Restaurant</dt>
            <dd>{restaurantMenuEntity.restaurant ? restaurantMenuEntity.restaurant.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/restaurant-menu" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/restaurant-menu/${restaurantMenuEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ restaurantMenu }: IRootState) => ({
  restaurantMenuEntity: restaurantMenu.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RestaurantMenuDetail);
