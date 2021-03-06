import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, InputGroup, Col, Row, Table } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudSearchAction, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getSearchEntities, getEntities } from './recipe-ingredient.reducer';
import { IRecipeIngredient } from 'app/shared/model/recipe-ingredient.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRecipeIngredientProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IRecipeIngredientState {
  search: string;
}

export class RecipeIngredient extends React.Component<IRecipeIngredientProps, IRecipeIngredientState> {
  state: IRecipeIngredientState = {
    search: ''
  };

  componentDidMount() {
    this.props.getEntities();
  }

  search = () => {
    if (this.state.search) {
      this.props.getSearchEntities(this.state.search);
    }
  };

  clear = () => {
    this.setState({ search: '' }, () => {
      this.props.getEntities();
    });
  };

  handleSearch = event => this.setState({ search: event.target.value });

  render() {
    const { recipeIngredientList, match } = this.props;
    return (
      <div>
        <h2 id="recipe-ingredient-heading">
          Recipe Ingredients
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Recipe Ingredient
          </Link>
        </h2>
        <Row>
          <Col sm="12">
            <AvForm onSubmit={this.search}>
              <AvGroup>
                <InputGroup>
                  <AvInput type="text" name="search" value={this.state.search} onChange={this.handleSearch} placeholder="Search" />
                  <Button className="input-group-addon">
                    <FontAwesomeIcon icon="search" />
                  </Button>
                  <Button type="reset" className="input-group-addon" onClick={this.clear}>
                    <FontAwesomeIcon icon="trash" />
                  </Button>
                </InputGroup>
              </AvGroup>
            </AvForm>
          </Col>
        </Row>
        <div className="table-responsive">
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Amount</th>
                <th>Ingredient Nutrition Value</th>
                <th>Restriction</th>
                <th>Recipe</th>
                <th>Measurement</th>
                <th>Ingredient</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {recipeIngredientList.map((recipeIngredient, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${recipeIngredient.id}`} color="link" size="sm">
                      {recipeIngredient.id}
                    </Button>
                  </td>
                  <td>{recipeIngredient.amount}</td>
                  <td>{recipeIngredient.ingredientNutritionValue}</td>
                  <td>{recipeIngredient.restriction}</td>
                  <td>
                    {recipeIngredient.recipe ? <Link to={`recipe/${recipeIngredient.recipe.id}`}>{recipeIngredient.recipe.id}</Link> : ''}
                  </td>
                  <td>
                    {recipeIngredient.measurement ? (
                      <Link to={`measurement/${recipeIngredient.measurement.id}`}>{recipeIngredient.measurement.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {recipeIngredient.ingredient ? (
                      <Link to={`ingredient/${recipeIngredient.ingredient.id}`}>{recipeIngredient.ingredient.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${recipeIngredient.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${recipeIngredient.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${recipeIngredient.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ recipeIngredient }: IRootState) => ({
  recipeIngredientList: recipeIngredient.entities
});

const mapDispatchToProps = {
  getSearchEntities,
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RecipeIngredient);
