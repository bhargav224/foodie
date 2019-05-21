import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IRecipe } from 'app/shared/model/recipe.model';
import { getEntities as getRecipes } from 'app/entities/recipe/recipe.reducer';
import { IMenuItem } from 'app/shared/model/menu-item.model';
import { getEntities as getMenuItems } from 'app/entities/menu-item/menu-item.reducer';
import { getEntity, updateEntity, createEntity, reset } from './menu-recipe.reducer';
import { IMenuRecipe } from 'app/shared/model/menu-recipe.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IMenuRecipeUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IMenuRecipeUpdateState {
  isNew: boolean;
  recipeId: string;
  menuItemId: string;
}

export class MenuRecipeUpdate extends React.Component<IMenuRecipeUpdateProps, IMenuRecipeUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      recipeId: '0',
      menuItemId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getRecipes();
    this.props.getMenuItems();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { menuRecipeEntity } = this.props;
      const entity = {
        ...menuRecipeEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/menu-recipe');
  };

  render() {
    const { menuRecipeEntity, recipes, menuItems, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="foodieChefApp.menuRecipe.home.createOrEditLabel">Create or edit a MenuRecipe</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : menuRecipeEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="menu-recipe-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label for="recipe.id">Recipe</Label>
                  <AvInput id="menu-recipe-recipe" type="select" className="form-control" name="recipe.id">
                    <option value="" key="0" />
                    {recipes
                      ? recipes.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="menuItem.id">Menu Item</Label>
                  <AvInput id="menu-recipe-menuItem" type="select" className="form-control" name="menuItem.id">
                    <option value="" key="0" />
                    {menuItems
                      ? menuItems.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/menu-recipe" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">Back</span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp; Save
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  recipes: storeState.recipe.entities,
  menuItems: storeState.menuItem.entities,
  menuRecipeEntity: storeState.menuRecipe.entity,
  loading: storeState.menuRecipe.loading,
  updating: storeState.menuRecipe.updating,
  updateSuccess: storeState.menuRecipe.updateSuccess
});

const mapDispatchToProps = {
  getRecipes,
  getMenuItems,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(MenuRecipeUpdate);
