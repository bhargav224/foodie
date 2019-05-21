import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './recipe-image.reducer';
import { IRecipeImage } from 'app/shared/model/recipe-image.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRecipeImageDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class RecipeImageDetail extends React.Component<IRecipeImageDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { recipeImageEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            RecipeImage [<b>{recipeImageEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="imagePath">Image Path</span>
            </dt>
            <dd>{recipeImageEntity.imagePath}</dd>
            <dt>Recipe</dt>
            <dd>{recipeImageEntity.recipe ? recipeImageEntity.recipe.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/recipe-image" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/recipe-image/${recipeImageEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ recipeImage }: IRootState) => ({
  recipeImageEntity: recipeImage.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RecipeImageDetail);
