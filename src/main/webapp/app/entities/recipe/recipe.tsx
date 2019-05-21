import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, InputGroup, Col, Row, Table } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudSearchAction, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getSearchEntities, getEntities } from './recipe.reducer';
import { IRecipe } from 'app/shared/model/recipe.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRecipeProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IRecipeState {
  search: string;
}

export class Recipe extends React.Component<IRecipeProps, IRecipeState> {
  state: IRecipeState = {
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
    const { recipeList, match } = this.props;
    return (
      <div>
        <h2 id="recipe-heading">
          Recipes
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Recipe
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
                <th>Calories Per Servings</th>
                <th>Cook Time</th>
                <th>Description</th>
                <th>Prep Time</th>
                <th>Published</th>
                <th>Rating</th>
                <th>Ready In</th>
                <th>Title</th>
                <th>Video</th>
                <th>Yields</th>
                <th>Level</th>
                <th>Food Categorie</th>
                <th>Cusine</th>
                <th>Course</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {recipeList.map((recipe, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${recipe.id}`} color="link" size="sm">
                      {recipe.id}
                    </Button>
                  </td>
                  <td>{recipe.amount}</td>
                  <td>{recipe.caloriesPerServings}</td>
                  <td>{recipe.cookTime}</td>
                  <td>{recipe.description}</td>
                  <td>{recipe.prepTime}</td>
                  <td>{recipe.published ? 'true' : 'false'}</td>
                  <td>{recipe.rating}</td>
                  <td>{recipe.readyIn}</td>
                  <td>{recipe.title}</td>
                  <td>{recipe.video}</td>
                  <td>{recipe.yields}</td>
                  <td>{recipe.level ? <Link to={`level/${recipe.level.id}`}>{recipe.level.id}</Link> : ''}</td>
                  <td>
                    {recipe.foodCategorie ? <Link to={`food-categorie/${recipe.foodCategorie.id}`}>{recipe.foodCategorie.id}</Link> : ''}
                  </td>
                  <td>{recipe.cusine ? <Link to={`cusine/${recipe.cusine.id}`}>{recipe.cusine.id}</Link> : ''}</td>
                  <td>{recipe.course ? <Link to={`course/${recipe.course.id}`}>{recipe.course.id}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${recipe.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${recipe.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${recipe.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ recipe }: IRootState) => ({
  recipeList: recipe.entities
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
)(Recipe);
