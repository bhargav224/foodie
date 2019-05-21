import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './menu-item.reducer';
import { IMenuItem } from 'app/shared/model/menu-item.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMenuItemDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class MenuItemDetail extends React.Component<IMenuItemDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { menuItemEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            MenuItem [<b>{menuItemEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="active">Active</span>
            </dt>
            <dd>{menuItemEntity.active ? 'true' : 'false'}</dd>
            <dt>
              <span id="description">Description</span>
            </dt>
            <dd>{menuItemEntity.description}</dd>
            <dt>
              <span id="imagePath">Image Path</span>
            </dt>
            <dd>{menuItemEntity.imagePath}</dd>
            <dt>
              <span id="name">Name</span>
            </dt>
            <dd>{menuItemEntity.name}</dd>
            <dt>Food Categorie</dt>
            <dd>{menuItemEntity.foodCategorie ? menuItemEntity.foodCategorie.id : ''}</dd>
            <dt>Course</dt>
            <dd>{menuItemEntity.course ? menuItemEntity.course.id : ''}</dd>
            <dt>Cusine</dt>
            <dd>{menuItemEntity.cusine ? menuItemEntity.cusine.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/menu-item" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/menu-item/${menuItemEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ menuItem }: IRootState) => ({
  menuItemEntity: menuItem.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(MenuItemDetail);
