import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './footnote.reducer';
import { IFootnote } from 'app/shared/model/footnote.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IFootnoteDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class FootnoteDetail extends React.Component<IFootnoteDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { footnoteEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Footnote [<b>{footnoteEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="footnote">Footnote</span>
            </dt>
            <dd>{footnoteEntity.footnote}</dd>
            <dt>Recipe</dt>
            <dd>{footnoteEntity.recipe ? footnoteEntity.recipe.id : ''}</dd>
            <dt>User Info</dt>
            <dd>{footnoteEntity.userInfo ? footnoteEntity.userInfo.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/footnote" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/footnote/${footnoteEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ footnote }: IRootState) => ({
  footnoteEntity: footnote.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(FootnoteDetail);
