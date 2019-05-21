import React from 'react';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from '../header-components';

export const EntitiesMenu = props => (
  // tslint:disable-next-line:jsx-self-close
  <NavDropdown icon="th-list" name="Entities" id="entity-menu">
    <DropdownItem tag={Link} to="/entity/recipe">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Recipe
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/recipe-step">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Recipe Step
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/recipe-has-step">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Recipe Has Step
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/recipe-image">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Recipe Image
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/level">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Level
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/food-categorie">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Food Categorie
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/course">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Course
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/cusine">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Cusine
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/menu-item">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Menu Item
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/restaurant">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Restaurant
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/address">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Address
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/restaurant-menu">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Restaurant Menu
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/menu-recipe">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Menu Recipe
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/user-info">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;User Info
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/recipe-comment">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Recipe Comment
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/recipe-rating">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Recipe Rating
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/share-recipe">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Share Recipe
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/recipe-like">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Recipe Like
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/footnote">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Footnote
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/invite-email">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Invite Email
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/invite-contact">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Invite Contact
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/user-rating">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;User Rating
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/chef-profile">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Chef Profile
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/nutrition-information">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Nutrition Information
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/ingredient-nutrition-info">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Ingredient Nutrition Info
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/measurement">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Measurement
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/ingredient">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Ingredient
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/recipe-ingredient">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Recipe Ingredient
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/role">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Role
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/user-info-role">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;User Info Role
    </DropdownItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
