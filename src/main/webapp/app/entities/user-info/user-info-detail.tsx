import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './user-info.reducer';
import { IUserInfo } from 'app/shared/model/user-info.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IUserInfoDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class UserInfoDetail extends React.Component<IUserInfoDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { userInfoEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            UserInfo [<b>{userInfoEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="authenticated">Authenticated</span>
            </dt>
            <dd>{userInfoEntity.authenticated ? 'true' : 'false'}</dd>
            <dt>
              <span id="contact">Contact</span>
            </dt>
            <dd>{userInfoEntity.contact}</dd>
            <dt>
              <span id="currentLoggedIn">Current Logged In</span>
            </dt>
            <dd>
              <TextFormat value={userInfoEntity.currentLoggedIn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="email">Email</span>
            </dt>
            <dd>{userInfoEntity.email}</dd>
            <dt>
              <span id="emailConfirmationSentOn">Email Confirmation Sent On</span>
            </dt>
            <dd>
              <TextFormat value={userInfoEntity.emailConfirmationSentOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="emailConfirmed">Email Confirmed</span>
            </dt>
            <dd>{userInfoEntity.emailConfirmed ? 'true' : 'false'}</dd>
            <dt>
              <span id="emailConfirmedOn">Email Confirmed On</span>
            </dt>
            <dd>
              <TextFormat value={userInfoEntity.emailConfirmedOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="lastLoggedIn">Last Logged In</span>
            </dt>
            <dd>
              <TextFormat value={userInfoEntity.lastLoggedIn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="firstName">First Name</span>
            </dt>
            <dd>{userInfoEntity.firstName}</dd>
            <dt>
              <span id="lastName">Last Name</span>
            </dt>
            <dd>{userInfoEntity.lastName}</dd>
            <dt>
              <span id="password">Password</span>
            </dt>
            <dd>{userInfoEntity.password}</dd>
            <dt>
              <span id="photo">Photo</span>
            </dt>
            <dd>{userInfoEntity.photo}</dd>
            <dt>
              <span id="registeredOn">Registered On</span>
            </dt>
            <dd>
              <TextFormat value={userInfoEntity.registeredOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>User Info</dt>
            <dd>{userInfoEntity.userInfo ? userInfoEntity.userInfo.id : ''}</dd>
            <dt>Chef Profile</dt>
            <dd>{userInfoEntity.chefProfile ? userInfoEntity.chefProfile.id : ''}</dd>
            <dt>Restaurant</dt>
            <dd>{userInfoEntity.restaurant ? userInfoEntity.restaurant.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/user-info" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/user-info/${userInfoEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ userInfo }: IRootState) => ({
  userInfoEntity: userInfo.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(UserInfoDetail);
