import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IItem } from 'app/shared/model/item.model';
import { getEntities } from './item.reducer';

export const Item = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const itemList = useAppSelector(state => state.item.entities);
  const loading = useAppSelector(state => state.item.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="item-heading" data-cy="ItemHeading">
        <Translate contentKey="myCollectionMonitorApp.item.home.title">Items</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="myCollectionMonitorApp.item.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/item/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="myCollectionMonitorApp.item.home.createLabel">Create new Item</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {itemList && itemList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="myCollectionMonitorApp.item.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="myCollectionMonitorApp.item.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="myCollectionMonitorApp.item.releaseDate">Release Date</Translate>
                </th>
                <th>
                  <Translate contentKey="myCollectionMonitorApp.item.releasePrice">Release Price</Translate>
                </th>
                <th>
                  <Translate contentKey="myCollectionMonitorApp.item.purchaseDate">Purchase Date</Translate>
                </th>
                <th>
                  <Translate contentKey="myCollectionMonitorApp.item.purchasePrice">Purchase Price</Translate>
                </th>
                <th>
                  <Translate contentKey="myCollectionMonitorApp.item.marketPrice">Market Price</Translate>
                </th>
                <th>
                  <Translate contentKey="myCollectionMonitorApp.item.collectionCategory">Collection Category</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {itemList.map((item, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/item/${item.id}`} color="link" size="sm">
                      {item.id}
                    </Button>
                  </td>
                  <td>{item.name}</td>
                  <td>{item.releaseDate ? <TextFormat type="date" value={item.releaseDate} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{item.releasePrice}</td>
                  <td>{item.purchaseDate ? <TextFormat type="date" value={item.purchaseDate} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{item.purchasePrice}</td>
                  <td>{item.marketPrice}</td>
                  <td>
                    {item.collectionCategory ? (
                      <Link to={`/collection-category/${item.collectionCategory.id}`}>{item.collectionCategory.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/item/${item.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/item/${item.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/item/${item.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="myCollectionMonitorApp.item.home.notFound">No Items found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Item;
