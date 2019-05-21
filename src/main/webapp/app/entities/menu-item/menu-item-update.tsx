import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IFoodCategorie } from 'app/shared/model/food-categorie.model';
import { getEntities as getFoodCategories } from 'app/entities/food-categorie/food-categorie.reducer';
import { ICourse } from 'app/shared/model/course.model';
import { getEntities as getCourses } from 'app/entities/course/course.reducer';
import { ICusine } from 'app/shared/model/cusine.model';
import { getEntities as getCusines } from 'app/entities/cusine/cusine.reducer';
import { getEntity, updateEntity, createEntity, reset } from './menu-item.reducer';
import { IMenuItem } from 'app/shared/model/menu-item.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IMenuItemUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IMenuItemUpdateState {
  isNew: boolean;
  foodCategorieId: string;
  courseId: string;
  cusineId: string;
}

export class MenuItemUpdate extends React.Component<IMenuItemUpdateProps, IMenuItemUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      foodCategorieId: '0',
      courseId: '0',
      cusineId: '0',
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

    this.props.getFoodCategories();
    this.props.getCourses();
    this.props.getCusines();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { menuItemEntity } = this.props;
      const entity = {
        ...menuItemEntity,
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
    this.props.history.push('/entity/menu-item');
  };

  render() {
    const { menuItemEntity, foodCategories, courses, cusines, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="foodieChefApp.menuItem.home.createOrEditLabel">Create or edit a MenuItem</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : menuItemEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="menu-item-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="activeLabel" check>
                    <AvInput id="menu-item-active" type="checkbox" className="form-control" name="active" />
                    Active
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="descriptionLabel" for="description">
                    Description
                  </Label>
                  <AvField
                    id="menu-item-description"
                    type="text"
                    name="description"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="imagePathLabel" for="imagePath">
                    Image Path
                  </Label>
                  <AvField
                    id="menu-item-imagePath"
                    type="text"
                    name="imagePath"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="nameLabel" for="name">
                    Name
                  </Label>
                  <AvField
                    id="menu-item-name"
                    type="text"
                    name="name"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="foodCategorie.id">Food Categorie</Label>
                  <AvInput id="menu-item-foodCategorie" type="select" className="form-control" name="foodCategorie.id">
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
                  <Label for="course.id">Course</Label>
                  <AvInput id="menu-item-course" type="select" className="form-control" name="course.id">
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
                <AvGroup>
                  <Label for="cusine.id">Cusine</Label>
                  <AvInput id="menu-item-cusine" type="select" className="form-control" name="cusine.id">
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
                <Button tag={Link} id="cancel-save" to="/entity/menu-item" replace color="info">
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
  foodCategories: storeState.foodCategorie.entities,
  courses: storeState.course.entities,
  cusines: storeState.cusine.entities,
  menuItemEntity: storeState.menuItem.entity,
  loading: storeState.menuItem.loading,
  updating: storeState.menuItem.updating,
  updateSuccess: storeState.menuItem.updateSuccess
});

const mapDispatchToProps = {
  getFoodCategories,
  getCourses,
  getCusines,
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
)(MenuItemUpdate);
