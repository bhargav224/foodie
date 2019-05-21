import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './user-info-role.reducer';
import { IUserInfoRole } from 'app/shared/model/user-info-role.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IUserInfoRoleDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class UserInfoRoleDetail extends React.Component<IUserInfoRoleDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { userInfoRoleEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            UserInfoRole [<b>{userInfoRoleEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>Role</dt>
            <dd>{userInfoRoleEntity.role ? userInfoRoleEntity.role.id : ''}</dd>
            <dt>User Info</dt>
            <dd>{userInfoRoleEntity.userInfo ? userInfoRoleEntity.userInfo.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/user-info-role" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/user-info-role/${userInfoRoleEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ userInfoRole }: IRootState) => ({
  userInfoRoleEntity: userInfoRole.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(UserInfoRoleDetail);
