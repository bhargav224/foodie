import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './share-recipe.reducer';
import { IShareRecipe } from 'app/shared/model/share-recipe.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IShareRecipeDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ShareRecipeDetail extends React.Component<IShareRecipeDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { shareRecipeEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            ShareRecipe [<b>{shareRecipeEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="date">Date</span>
            </dt>
            <dd>
              <TextFormat value={shareRecipeEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>Recipe</dt>
            <dd>{shareRecipeEntity.recipe ? shareRecipeEntity.recipe.id : ''}</dd>
            <dt>Shared By</dt>
            <dd>{shareRecipeEntity.sharedBy ? shareRecipeEntity.sharedBy.id : ''}</dd>
            <dt>Shared To</dt>
            <dd>{shareRecipeEntity.sharedTo ? shareRecipeEntity.sharedTo.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/share-recipe" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/share-recipe/${shareRecipeEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ shareRecipe }: IRootState) => ({
  shareRecipeEntity: shareRecipe.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ShareRecipeDetail);
