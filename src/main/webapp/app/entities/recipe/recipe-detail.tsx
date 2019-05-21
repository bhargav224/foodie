import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './recipe.reducer';
import { IRecipe } from 'app/shared/model/recipe.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRecipeDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class RecipeDetail extends React.Component<IRecipeDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { recipeEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Recipe [<b>{recipeEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="amount">Amount</span>
            </dt>
            <dd>{recipeEntity.amount}</dd>
            <dt>
              <span id="caloriesPerServings">Calories Per Servings</span>
            </dt>
            <dd>{recipeEntity.caloriesPerServings}</dd>
            <dt>
              <span id="cookTime">Cook Time</span>
            </dt>
            <dd>{recipeEntity.cookTime}</dd>
            <dt>
              <span id="description">Description</span>
            </dt>
            <dd>{recipeEntity.description}</dd>
            <dt>
              <span id="prepTime">Prep Time</span>
            </dt>
            <dd>{recipeEntity.prepTime}</dd>
            <dt>
              <span id="published">Published</span>
            </dt>
            <dd>{recipeEntity.published ? 'true' : 'false'}</dd>
            <dt>
              <span id="rating">Rating</span>
            </dt>
            <dd>{recipeEntity.rating}</dd>
            <dt>
              <span id="readyIn">Ready In</span>
            </dt>
            <dd>{recipeEntity.readyIn}</dd>
            <dt>
              <span id="title">Title</span>
            </dt>
            <dd>{recipeEntity.title}</dd>
            <dt>
              <span id="video">Video</span>
            </dt>
            <dd>{recipeEntity.video}</dd>
            <dt>
              <span id="yields">Yields</span>
            </dt>
            <dd>{recipeEntity.yields}</dd>
            <dt>Level</dt>
            <dd>{recipeEntity.level ? recipeEntity.level.id : ''}</dd>
            <dt>Food Categorie</dt>
            <dd>{recipeEntity.foodCategorie ? recipeEntity.foodCategorie.id : ''}</dd>
            <dt>Cusine</dt>
            <dd>{recipeEntity.cusine ? recipeEntity.cusine.id : ''}</dd>
            <dt>Course</dt>
            <dd>{recipeEntity.course ? recipeEntity.course.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/recipe" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/recipe/${recipeEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ recipe }: IRootState) => ({
  recipeEntity: recipe.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RecipeDetail);
