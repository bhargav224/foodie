import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IMenuItem } from 'app/shared/model/menu-item.model';
import { getEntities as getMenuItems } from 'app/entities/menu-item/menu-item.reducer';
import { IRestaurant } from 'app/shared/model/restaurant.model';
import { getEntities as getRestaurants } from 'app/entities/restaurant/restaurant.reducer';
import { getEntity, updateEntity, createEntity, reset } from './restaurant-menu.reducer';
import { IRestaurantMenu } from 'app/shared/model/restaurant-menu.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IRestaurantMenuUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IRestaurantMenuUpdateState {
  isNew: boolean;
  menuItemId: string;
  restaurantId: string;
}

export class RestaurantMenuUpdate extends React.Component<IRestaurantMenuUpdateProps, IRestaurantMenuUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      menuItemId: '0',
      restaurantId: '0',
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

    this.props.getMenuItems();
    this.props.getRestaurants();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { restaurantMenuEntity } = this.props;
      const entity = {
        ...restaurantMenuEntity,
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
    this.props.history.push('/entity/restaurant-menu');
  };

  render() {
    const { restaurantMenuEntity, menuItems, restaurants, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="foodieChefApp.restaurantMenu.home.createOrEditLabel">Create or edit a RestaurantMenu</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : restaurantMenuEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="restaurant-menu-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label for="menuItem.id">Menu Item</Label>
                  <AvInput id="restaurant-menu-menuItem" type="select" className="form-control" name="menuItem.id">
                    <option value="" key="0" />
                    {menuItems
                      ? menuItems.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="restaurant.id">Restaurant</Label>
                  <AvInput id="restaurant-menu-restaurant" type="select" className="form-control" name="restaurant.id">
                    <option value="" key="0" />
                    {restaurants
                      ? restaurants.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/restaurant-menu" replace color="info">
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
  menuItems: storeState.menuItem.entities,
  restaurants: storeState.restaurant.entities,
  restaurantMenuEntity: storeState.restaurantMenu.entity,
  loading: storeState.restaurantMenu.loading,
  updating: storeState.restaurantMenu.updating,
  updateSuccess: storeState.restaurantMenu.updateSuccess
});

const mapDispatchToProps = {
  getMenuItems,
  getRestaurants,
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
)(RestaurantMenuUpdate);
