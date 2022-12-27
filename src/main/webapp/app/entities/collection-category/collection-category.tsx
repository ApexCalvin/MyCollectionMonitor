import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICollectionCategory } from 'app/shared/model/collection-category.model';
import { getEntities } from './collection-category.reducer';

export const CollectionCategory = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const collectionCategoryList = useAppSelector(state => state.collectionCategory.entities);
  const loading = useAppSelector(state => state.collectionCategory.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="collection-category-heading" data-cy="CollectionCategoryHeading">
        <Translate contentKey="myCollectionMonitorApp.collectionCategory.home.title">Collection Categories</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="myCollectionMonitorApp.collectionCategory.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/collection-category/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="myCollectionMonitorApp.collectionCategory.home.createLabel">Create new Collection Category</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {collectionCategoryList && collectionCategoryList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="myCollectionMonitorApp.collectionCategory.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="myCollectionMonitorApp.collectionCategory.categoryName">Category Name</Translate>
                </th>
                <th>
                  <Translate contentKey="myCollectionMonitorApp.collectionCategory.user">User</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {collectionCategoryList.map((collectionCategory, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/collection-category/${collectionCategory.id}`} color="link" size="sm">
                      {collectionCategory.id}
                    </Button>
                  </td>
                  <td>{collectionCategory.categoryName}</td>
                  <td>{collectionCategory.user ? collectionCategory.user.login : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/collection-category/${collectionCategory.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/collection-category/${collectionCategory.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/collection-category/${collectionCategory.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
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
              <Translate contentKey="myCollectionMonitorApp.collectionCategory.home.notFound">No Collection Categories found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default CollectionCategory;
