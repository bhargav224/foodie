import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './user-rating.reducer';
import { IUserRating } from 'app/shared/model/user-rating.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IUserRatingDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class UserRatingDetail extends React.Component<IUserRatingDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { userRatingEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            UserRating [<b>{userRatingEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="date">Date</span>
            </dt>
            <dd>
              <TextFormat value={userRatingEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="rating">Rating</span>
            </dt>
            <dd>{userRatingEntity.rating}</dd>
            <dt>Rate By User</dt>
            <dd>{userRatingEntity.rateByUser ? userRatingEntity.rateByUser.id : ''}</dd>
            <dt>Rate To User</dt>
            <dd>{userRatingEntity.rateToUser ? userRatingEntity.rateToUser.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/user-rating" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/user-rating/${userRatingEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ userRating }: IRootState) => ({
  userRatingEntity: userRating.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(UserRatingDetail);
