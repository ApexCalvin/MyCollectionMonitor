import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICollectionCategory } from 'app/shared/model/collection-category.model';
import { getEntities as getCollectionCategories } from 'app/entities/collection-category/collection-category.reducer';
import { IItem } from 'app/shared/model/item.model';
import { getEntity, updateEntity, createEntity, reset } from './item.reducer';

export const ItemUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const collectionCategories = useAppSelector(state => state.collectionCategory.entities);
  const itemEntity = useAppSelector(state => state.item.entity);
  const loading = useAppSelector(state => state.item.loading);
  const updating = useAppSelector(state => state.item.updating);
  const updateSuccess = useAppSelector(state => state.item.updateSuccess);

  const handleClose = () => {
    navigate('/item');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCollectionCategories({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...itemEntity,
      ...values,
      collectionCategory: collectionCategories.find(it => it.id.toString() === values.collectionCategory.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...itemEntity,
          collectionCategory: itemEntity?.collectionCategory?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="myCollectionMonitorApp.item.home.createOrEditLabel" data-cy="ItemCreateUpdateHeading">
            <Translate contentKey="myCollectionMonitorApp.item.home.createOrEditLabel">Create or edit a Item</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="item-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('myCollectionMonitorApp.item.name')} id="item-name" name="name" data-cy="name" type="text" />
              <ValidatedField
                label={translate('myCollectionMonitorApp.item.releaseDate')}
                id="item-releaseDate"
                name="releaseDate"
                data-cy="releaseDate"
                type="date"
              />
              <ValidatedField
                label={translate('myCollectionMonitorApp.item.releasePrice')}
                id="item-releasePrice"
                name="releasePrice"
                data-cy="releasePrice"
                type="text"
              />
              <ValidatedField
                label={translate('myCollectionMonitorApp.item.purchaseDate')}
                id="item-purchaseDate"
                name="purchaseDate"
                data-cy="purchaseDate"
                type="date"
              />
              <ValidatedField
                label={translate('myCollectionMonitorApp.item.purchasePrice')}
                id="item-purchasePrice"
                name="purchasePrice"
                data-cy="purchasePrice"
                type="text"
              />
              <ValidatedField
                label={translate('myCollectionMonitorApp.item.marketPrice')}
                id="item-marketPrice"
                name="marketPrice"
                data-cy="marketPrice"
                type="text"
              />
              <ValidatedField
                id="item-collectionCategory"
                name="collectionCategory"
                data-cy="collectionCategory"
                label={translate('myCollectionMonitorApp.item.collectionCategory')}
                type="select"
              >
                <option value="" key="0" />
                {collectionCategories
                  ? collectionCategories.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/item" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ItemUpdate;
