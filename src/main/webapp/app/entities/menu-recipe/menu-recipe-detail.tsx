import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './menu-recipe.reducer';
import { IMenuRecipe } from 'app/shared/model/menu-recipe.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMenuRecipeDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class MenuRecipeDetail extends React.Component<IMenuRecipeDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { menuRecipeEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            MenuRecipe [<b>{menuRecipeEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>Recipe</dt>
            <dd>{menuRecipeEntity.recipe ? menuRecipeEntity.recipe.id : ''}</dd>
            <dt>Menu Item</dt>
            <dd>{menuRecipeEntity.menuItem ? menuRecipeEntity.menuItem.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/menu-recipe" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/menu-recipe/${menuRecipeEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ menuRecipe }: IRootState) => ({
  menuRecipeEntity: menuRecipe.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(MenuRecipeDetail);
