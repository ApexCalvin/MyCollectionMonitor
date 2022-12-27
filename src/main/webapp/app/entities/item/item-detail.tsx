import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './item.reducer';

export const ItemDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const itemEntity = useAppSelector(state => state.item.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="itemDetailsHeading">
          <Translate contentKey="myCollectionMonitorApp.item.detail.title">Item</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{itemEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="myCollectionMonitorApp.item.name">Name</Translate>
            </span>
          </dt>
          <dd>{itemEntity.name}</dd>
          <dt>
            <span id="releaseDate">
              <Translate contentKey="myCollectionMonitorApp.item.releaseDate">Release Date</Translate>
            </span>
          </dt>
          <dd>
            {itemEntity.releaseDate ? <TextFormat value={itemEntity.releaseDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="releasePrice">
              <Translate contentKey="myCollectionMonitorApp.item.releasePrice">Release Price</Translate>
            </span>
          </dt>
          <dd>{itemEntity.releasePrice}</dd>
          <dt>
            <span id="purchaseDate">
              <Translate contentKey="myCollectionMonitorApp.item.purchaseDate">Purchase Date</Translate>
            </span>
          </dt>
          <dd>
            {itemEntity.purchaseDate ? <TextFormat value={itemEntity.purchaseDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="purchasePrice">
              <Translate contentKey="myCollectionMonitorApp.item.purchasePrice">Purchase Price</Translate>
            </span>
          </dt>
          <dd>{itemEntity.purchasePrice}</dd>
          <dt>
            <span id="marketPrice">
              <Translate contentKey="myCollectionMonitorApp.item.marketPrice">Market Price</Translate>
            </span>
          </dt>
          <dd>{itemEntity.marketPrice}</dd>
          <dt>
            <Translate contentKey="myCollectionMonitorApp.item.collectionCategory">Collection Category</Translate>
          </dt>
          <dd>{itemEntity.collectionCategory ? itemEntity.collectionCategory.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/item" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/item/${itemEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ItemDetail;
