import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './chef-profile.reducer';
import { IChefProfile } from 'app/shared/model/chef-profile.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IChefProfileDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ChefProfileDetail extends React.Component<IChefProfileDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { chefProfileEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            ChefProfile [<b>{chefProfileEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="photo">Photo</span>
            </dt>
            <dd>{chefProfileEntity.photo}</dd>
            <dt>
              <span id="speciality">Speciality</span>
            </dt>
            <dd>{chefProfileEntity.speciality}</dd>
            <dt>
              <span id="type">Type</span>
            </dt>
            <dd>{chefProfileEntity.type}</dd>
            <dt>
              <span id="website">Website</span>
            </dt>
            <dd>{chefProfileEntity.website}</dd>
          </dl>
          <Button tag={Link} to="/entity/chef-profile" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/chef-profile/${chefProfileEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ chefProfile }: IRootState) => ({
  chefProfileEntity: chefProfile.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ChefProfileDetail);
