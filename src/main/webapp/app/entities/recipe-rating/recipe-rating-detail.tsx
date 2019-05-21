import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './recipe-rating.reducer';
import { IRecipeRating } from 'app/shared/model/recipe-rating.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRecipeRatingDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class RecipeRatingDetail extends React.Component<IRecipeRatingDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { recipeRatingEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            RecipeRating [<b>{recipeRatingEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="date">Date</span>
            </dt>
            <dd>
              <TextFormat value={recipeRatingEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="rating">Rating</span>
            </dt>
            <dd>{recipeRatingEntity.rating}</dd>
            <dt>Recipe</dt>
            <dd>{recipeRatingEntity.recipe ? recipeRatingEntity.recipe.id : ''}</dd>
            <dt>User Info</dt>
            <dd>{recipeRatingEntity.userInfo ? recipeRatingEntity.userInfo.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/recipe-rating" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/recipe-rating/${recipeRatingEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ recipeRating }: IRootState) => ({
  recipeRatingEntity: recipeRating.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RecipeRatingDetail);
