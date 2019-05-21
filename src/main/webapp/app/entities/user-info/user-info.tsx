import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, InputGroup, Col, Row, Table } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudSearchAction, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getSearchEntities, getEntities } from './user-info.reducer';
import { IUserInfo } from 'app/shared/model/user-info.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IUserInfoProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IUserInfoState {
  search: string;
}

export class UserInfo extends React.Component<IUserInfoProps, IUserInfoState> {
  state: IUserInfoState = {
    search: ''
  };

  componentDidMount() {
    this.props.getEntities();
  }

  search = () => {
    if (this.state.search) {
      this.props.getSearchEntities(this.state.search);
    }
  };

  clear = () => {
    this.setState({ search: '' }, () => {
      this.props.getEntities();
    });
  };

  handleSearch = event => this.setState({ search: event.target.value });

  render() {
    const { userInfoList, match } = this.props;
    return (
      <div>
        <h2 id="user-info-heading">
          User Infos
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new User Info
          </Link>
        </h2>
        <Row>
          <Col sm="12">
            <AvForm onSubmit={this.search}>
              <AvGroup>
                <InputGroup>
                  <AvInput type="text" name="search" value={this.state.search} onChange={this.handleSearch} placeholder="Search" />
                  <Button className="input-group-addon">
                    <FontAwesomeIcon icon="search" />
                  </Button>
                  <Button type="reset" className="input-group-addon" onClick={this.clear}>
                    <FontAwesomeIcon icon="trash" />
                  </Button>
                </InputGroup>
              </AvGroup>
            </AvForm>
          </Col>
        </Row>
        <div className="table-responsive">
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Authenticated</th>
                <th>Contact</th>
                <th>Current Logged In</th>
                <th>Email</th>
                <th>Email Confirmation Sent On</th>
                <th>Email Confirmed</th>
                <th>Email Confirmed On</th>
                <th>Last Logged In</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Password</th>
                <th>Photo</th>
                <th>Registered On</th>
                <th>User Info</th>
                <th>Chef Profile</th>
                <th>Restaurant</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {userInfoList.map((userInfo, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${userInfo.id}`} color="link" size="sm">
                      {userInfo.id}
                    </Button>
                  </td>
                  <td>{userInfo.authenticated ? 'true' : 'false'}</td>
                  <td>{userInfo.contact}</td>
                  <td>
                    <TextFormat type="date" value={userInfo.currentLoggedIn} format={APP_LOCAL_DATE_FORMAT} />
                  </td>
                  <td>{userInfo.email}</td>
                  <td>
                    <TextFormat type="date" value={userInfo.emailConfirmationSentOn} format={APP_LOCAL_DATE_FORMAT} />
                  </td>
                  <td>{userInfo.emailConfirmed ? 'true' : 'false'}</td>
                  <td>
                    <TextFormat type="date" value={userInfo.emailConfirmedOn} format={APP_LOCAL_DATE_FORMAT} />
                  </td>
                  <td>
                    <TextFormat type="date" value={userInfo.lastLoggedIn} format={APP_LOCAL_DATE_FORMAT} />
                  </td>
                  <td>{userInfo.firstName}</td>
                  <td>{userInfo.lastName}</td>
                  <td>{userInfo.password}</td>
                  <td>{userInfo.photo}</td>
                  <td>
                    <TextFormat type="date" value={userInfo.registeredOn} format={APP_LOCAL_DATE_FORMAT} />
                  </td>
                  <td>{userInfo.userInfo ? <Link to={`user-info/${userInfo.userInfo.id}`}>{userInfo.userInfo.id}</Link> : ''}</td>
                  <td>
                    {userInfo.chefProfile ? <Link to={`chef-profile/${userInfo.chefProfile.id}`}>{userInfo.chefProfile.id}</Link> : ''}
                  </td>
                  <td>{userInfo.restaurant ? <Link to={`restaurant/${userInfo.restaurant.id}`}>{userInfo.restaurant.id}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${userInfo.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${userInfo.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${userInfo.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ userInfo }: IRootState) => ({
  userInfoList: userInfo.entities
});

const mapDispatchToProps = {
  getSearchEntities,
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(UserInfo);
