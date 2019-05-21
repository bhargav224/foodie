import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntities as getUserInfos } from 'app/entities/user-info/user-info.reducer';
import { IChefProfile } from 'app/shared/model/chef-profile.model';
import { getEntities as getChefProfiles } from 'app/entities/chef-profile/chef-profile.reducer';
import { IRestaurant } from 'app/shared/model/restaurant.model';
import { getEntities as getRestaurants } from 'app/entities/restaurant/restaurant.reducer';
import { getEntity, updateEntity, createEntity, reset } from './user-info.reducer';
import { IUserInfo } from 'app/shared/model/user-info.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IUserInfoUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IUserInfoUpdateState {
  isNew: boolean;
  userInfoId: string;
  invitedById: string;
  chefProfileId: string;
  restaurantId: string;
}

export class UserInfoUpdate extends React.Component<IUserInfoUpdateProps, IUserInfoUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      userInfoId: '0',
      invitedById: '0',
      chefProfileId: '0',
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

    this.props.getUserInfos();
    this.props.getChefProfiles();
    this.props.getRestaurants();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { userInfoEntity } = this.props;
      const entity = {
        ...userInfoEntity,
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
    this.props.history.push('/entity/user-info');
  };

  render() {
    const { userInfoEntity, userInfos, chefProfiles, restaurants, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="foodieChefApp.userInfo.home.createOrEditLabel">Create or edit a UserInfo</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : userInfoEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="user-info-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="authenticatedLabel" check>
                    <AvInput id="user-info-authenticated" type="checkbox" className="form-control" name="authenticated" />
                    Authenticated
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="contactLabel" for="contact">
                    Contact
                  </Label>
                  <AvField
                    id="user-info-contact"
                    type="string"
                    className="form-control"
                    name="contact"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      number: { value: true, errorMessage: 'This field should be a number.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="currentLoggedInLabel" for="currentLoggedIn">
                    Current Logged In
                  </Label>
                  <AvField
                    id="user-info-currentLoggedIn"
                    type="date"
                    className="form-control"
                    name="currentLoggedIn"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="emailLabel" for="email">
                    Email
                  </Label>
                  <AvField
                    id="user-info-email"
                    type="text"
                    name="email"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="emailConfirmationSentOnLabel" for="emailConfirmationSentOn">
                    Email Confirmation Sent On
                  </Label>
                  <AvField
                    id="user-info-emailConfirmationSentOn"
                    type="date"
                    className="form-control"
                    name="emailConfirmationSentOn"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="emailConfirmedLabel" check>
                    <AvInput id="user-info-emailConfirmed" type="checkbox" className="form-control" name="emailConfirmed" />
                    Email Confirmed
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="emailConfirmedOnLabel" for="emailConfirmedOn">
                    Email Confirmed On
                  </Label>
                  <AvField
                    id="user-info-emailConfirmedOn"
                    type="date"
                    className="form-control"
                    name="emailConfirmedOn"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="lastLoggedInLabel" for="lastLoggedIn">
                    Last Logged In
                  </Label>
                  <AvField
                    id="user-info-lastLoggedIn"
                    type="date"
                    className="form-control"
                    name="lastLoggedIn"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="firstNameLabel" for="firstName">
                    First Name
                  </Label>
                  <AvField
                    id="user-info-firstName"
                    type="text"
                    name="firstName"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="lastNameLabel" for="lastName">
                    Last Name
                  </Label>
                  <AvField
                    id="user-info-lastName"
                    type="text"
                    name="lastName"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="passwordLabel" for="password">
                    Password
                  </Label>
                  <AvField
                    id="user-info-password"
                    type="text"
                    name="password"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="photoLabel" for="photo">
                    Photo
                  </Label>
                  <AvField
                    id="user-info-photo"
                    type="text"
                    name="photo"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="registeredOnLabel" for="registeredOn">
                    Registered On
                  </Label>
                  <AvField
                    id="user-info-registeredOn"
                    type="date"
                    className="form-control"
                    name="registeredOn"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="userInfo.id">User Info</Label>
                  <AvInput id="user-info-userInfo" type="select" className="form-control" name="userInfo.id">
                    <option value="" key="0" />
                    {userInfos
                      ? userInfos.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="chefProfile.id">Chef Profile</Label>
                  <AvInput id="user-info-chefProfile" type="select" className="form-control" name="chefProfile.id">
                    <option value="" key="0" />
                    {chefProfiles
                      ? chefProfiles.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="restaurant.id">Restaurant</Label>
                  <AvInput id="user-info-restaurant" type="select" className="form-control" name="restaurant.id">
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
                <Button tag={Link} id="cancel-save" to="/entity/user-info" replace color="info">
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
  userInfos: storeState.userInfo.entities,
  chefProfiles: storeState.chefProfile.entities,
  restaurants: storeState.restaurant.entities,
  userInfoEntity: storeState.userInfo.entity,
  loading: storeState.userInfo.loading,
  updating: storeState.userInfo.updating,
  updateSuccess: storeState.userInfo.updateSuccess
});

const mapDispatchToProps = {
  getUserInfos,
  getChefProfiles,
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
)(UserInfoUpdate);
