import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './recipe-comment.reducer';
import { IRecipeComment } from 'app/shared/model/recipe-comment.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRecipeCommentDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class RecipeCommentDetail extends React.Component<IRecipeCommentDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { recipeCommentEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            RecipeComment [<b>{recipeCommentEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="comment">Comment</span>
            </dt>
            <dd>{recipeCommentEntity.comment}</dd>
            <dt>
              <span id="date">Date</span>
            </dt>
            <dd>
              <TextFormat value={recipeCommentEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>Recipe</dt>
            <dd>{recipeCommentEntity.recipe ? recipeCommentEntity.recipe.id : ''}</dd>
            <dt>User Info</dt>
            <dd>{recipeCommentEntity.userInfo ? recipeCommentEntity.userInfo.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/recipe-comment" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/recipe-comment/${recipeCommentEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ recipeComment }: IRootState) => ({
  recipeCommentEntity: recipeComment.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RecipeCommentDetail);
