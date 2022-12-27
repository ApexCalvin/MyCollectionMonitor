import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './collection-category.reducer';

export const CollectionCategoryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const collectionCategoryEntity = useAppSelector(state => state.collectionCategory.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="collectionCategoryDetailsHeading">
          <Translate contentKey="myCollectionMonitorApp.collectionCategory.detail.title">CollectionCategory</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{collectionCategoryEntity.id}</dd>
          <dt>
            <span id="categoryName">
              <Translate contentKey="myCollectionMonitorApp.collectionCategory.categoryName">Category Name</Translate>
            </span>
          </dt>
          <dd>{collectionCategoryEntity.categoryName}</dd>
          <dt>
            <Translate contentKey="myCollectionMonitorApp.collectionCategory.user">User</Translate>
          </dt>
          <dd>{collectionCategoryEntity.user ? collectionCategoryEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/collection-category" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/collection-category/${collectionCategoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CollectionCategoryDetail;
