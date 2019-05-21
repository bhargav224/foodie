import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './invite-email.reducer';
import { IInviteEmail } from 'app/shared/model/invite-email.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IInviteEmailDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class InviteEmailDetail extends React.Component<IInviteEmailDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { inviteEmailEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            InviteEmail [<b>{inviteEmailEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="email">Email</span>
            </dt>
            <dd>{inviteEmailEntity.email}</dd>
            <dt>User Info</dt>
            <dd>{inviteEmailEntity.userInfo ? inviteEmailEntity.userInfo.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/invite-email" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/invite-email/${inviteEmailEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ inviteEmail }: IRootState) => ({
  inviteEmailEntity: inviteEmail.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(InviteEmailDetail);
