import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './invite-contact.reducer';
import { IInviteContact } from 'app/shared/model/invite-contact.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IInviteContactDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class InviteContactDetail extends React.Component<IInviteContactDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { inviteContactEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            InviteContact [<b>{inviteContactEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="contact">Contact</span>
            </dt>
            <dd>{inviteContactEntity.contact}</dd>
            <dt>User Info</dt>
            <dd>{inviteContactEntity.userInfo ? inviteContactEntity.userInfo.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/invite-contact" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/invite-contact/${inviteContactEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ inviteContact }: IRootState) => ({
  inviteContactEntity: inviteContact.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(InviteContactDetail);
