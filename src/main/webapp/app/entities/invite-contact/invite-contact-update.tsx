import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUserInfo } from 'app/shared/model/user-info.model';
import { getEntities as getUserInfos } from 'app/entities/user-info/user-info.reducer';
import { getEntity, updateEntity, createEntity, reset } from './invite-contact.reducer';
import { IInviteContact } from 'app/shared/model/invite-contact.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IInviteContactUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IInviteContactUpdateState {
  isNew: boolean;
  userInfoId: string;
}

export class InviteContactUpdate extends React.Component<IInviteContactUpdateProps, IInviteContactUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      userInfoId: '0',
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
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { inviteContactEntity } = this.props;
      const entity = {
        ...inviteContactEntity,
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
    this.props.history.push('/entity/invite-contact');
  };

  render() {
    const { inviteContactEntity, userInfos, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="foodieChefApp.inviteContact.home.createOrEditLabel">Create or edit a InviteContact</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : inviteContactEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="invite-contact-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="contactLabel" for="contact">
                    Contact
                  </Label>
                  <AvField
                    id="invite-contact-contact"
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
                  <Label for="userInfo.id">User Info</Label>
                  <AvInput id="invite-contact-userInfo" type="select" className="form-control" name="userInfo.id">
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
                <Button tag={Link} id="cancel-save" to="/entity/invite-contact" replace color="info">
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
  inviteContactEntity: storeState.inviteContact.entity,
  loading: storeState.inviteContact.loading,
  updating: storeState.inviteContact.updating,
  updateSuccess: storeState.inviteContact.updateSuccess
});

const mapDispatchToProps = {
  getUserInfos,
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
)(InviteContactUpdate);
