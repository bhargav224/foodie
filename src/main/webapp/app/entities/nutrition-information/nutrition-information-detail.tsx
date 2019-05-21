import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './nutrition-information.reducer';
import { INutritionInformation } from 'app/shared/model/nutrition-information.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface INutritionInformationDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class NutritionInformationDetail extends React.Component<INutritionInformationDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { nutritionInformationEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            NutritionInformation [<b>{nutritionInformationEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="active">Active</span>
            </dt>
            <dd>{nutritionInformationEntity.active ? 'true' : 'false'}</dd>
            <dt>
              <span id="nutrition">Nutrition</span>
            </dt>
            <dd>{nutritionInformationEntity.nutrition}</dd>
          </dl>
          <Button tag={Link} to="/entity/nutrition-information" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/nutrition-information/${nutritionInformationEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ nutritionInformation }: IRootState) => ({
  nutritionInformationEntity: nutritionInformation.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(NutritionInformationDetail);
