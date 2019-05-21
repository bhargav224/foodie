import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ILevel } from 'app/shared/model/level.model';
import { getEntities as getLevels } from 'app/entities/level/level.reducer';
import { IFoodCategorie } from 'app/shared/model/food-categorie.model';
import { getEntities as getFoodCategories } from 'app/entities/food-categorie/food-categorie.reducer';
import { ICusine } from 'app/shared/model/cusine.model';
import { getEntities as getCusines } from 'app/entities/cusine/cusine.reducer';
import { ICourse } from 'app/shared/model/course.model';
import { getEntities as getCourses } from 'app/entities/course/course.reducer';
import { getEntity, updateEntity, createEntity, reset } from './recipe.reducer';
import { IRecipe } from 'app/shared/model/recipe.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IRecipeUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IRecipeUpdateState {
  isNew: boolean;
  levelId: string;
  foodCategorieId: string;
  cusineId: string;
  courseId: string;
}

export class RecipeUpdate extends React.Component<IRecipeUpdateProps, IRecipeUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      levelId: '0',
      foodCategorieId: '0',
      cusineId: '0',
      courseId: '0',
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

    this.props.getLevels();
    this.props.getFoodCategories();
    this.props.getCusines();
    this.props.getCourses();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { recipeEntity } = this.props;
      const entity = {
        ...recipeEntity,
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
    this.props.history.push('/entity/recipe');
  };

  render() {
    const { recipeEntity, levels, foodCategories, cusines, courses, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="foodieChefApp.recipe.home.createOrEditLabel">Create or edit a Recipe</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : recipeEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="recipe-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="amountLabel" for="amount">
                    Amount
                  </Label>
                  <AvField
                    id="recipe-amount"
                    type="string"
                    className="form-control"
                    name="amount"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      number: { value: true, errorMessage: 'This field should be a number.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="caloriesPerServingsLabel" for="caloriesPerServings">
                    Calories Per Servings
                  </Label>
                  <AvField
                    id="recipe-caloriesPerServings"
                    type="string"
                    className="form-control"
                    name="caloriesPerServings"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      number: { value: true, errorMessage: 'This field should be a number.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="cookTimeLabel" for="cookTime">
                    Cook Time
                  </Label>
                  <AvField
                    id="recipe-cookTime"
                    type="string"
                    className="form-control"
                    name="cookTime"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      number: { value: true, errorMessage: 'This field should be a number.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="descriptionLabel" for="description">
                    Description
                  </Label>
                  <AvField
                    id="recipe-description"
                    type="text"
                    name="description"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="prepTimeLabel" for="prepTime">
                    Prep Time
                  </Label>
                  <AvField
                    id="recipe-prepTime"
                    type="string"
                    className="form-control"
                    name="prepTime"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      number: { value: true, errorMessage: 'This field should be a number.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="publishedLabel" check>
                    <AvInput id="recipe-published" type="checkbox" className="form-control" name="published" />
                    Published
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="ratingLabel" for="rating">
                    Rating
                  </Label>
                  <AvField
                    id="recipe-rating"
                    type="string"
                    className="form-control"
                    name="rating"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      number: { value: true, errorMessage: 'This field should be a number.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="readyInLabel" for="readyIn">
                    Ready In
                  </Label>
                  <AvField
                    id="recipe-readyIn"
                    type="string"
                    className="form-control"
                    name="readyIn"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      number: { value: true, errorMessage: 'This field should be a number.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="titleLabel" for="title">
                    Title
                  </Label>
                  <AvField
                    id="recipe-title"
                    type="text"
                    name="title"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="videoLabel" for="video">
                    Video
                  </Label>
                  <AvField
                    id="recipe-video"
                    type="text"
                    name="video"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="yieldsLabel" for="yields">
                    Yields
                  </Label>
                  <AvField
                    id="recipe-yields"
                    type="string"
                    className="form-control"
                    name="yields"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      number: { value: true, errorMessage: 'This field should be a number.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="level.id">Level</Label>
                  <AvInput id="recipe-level" type="select" className="form-control" name="level.id">
                    <option value="" key="0" />
                    {levels
                      ? levels.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="foodCategorie.id">Food Categorie</Label>
                  <AvInput id="recipe-foodCategorie" type="select" className="form-control" name="foodCategorie.id">
                    <option value="" key="0" />
                    {foodCategories
                      ? foodCategories.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="cusine.id">Cusine</Label>
                  <AvInput id="recipe-cusine" type="select" className="form-control" name="cusine.id">
                    <option value="" key="0" />
                    {cusines
                      ? cusines.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="course.id">Course</Label>
                  <AvInput id="recipe-course" type="select" className="form-control" name="course.id">
                    <option value="" key="0" />
                    {courses
                      ? courses.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/recipe" replace color="info">
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
  levels: storeState.level.entities,
  foodCategories: storeState.foodCategorie.entities,
  cusines: storeState.cusine.entities,
  courses: storeState.course.entities,
  recipeEntity: storeState.recipe.entity,
  loading: storeState.recipe.loading,
  updating: storeState.recipe.updating,
  updateSuccess: storeState.recipe.updateSuccess
});

const mapDispatchToProps = {
  getLevels,
  getFoodCategories,
  getCusines,
  getCourses,
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
)(RecipeUpdate);
