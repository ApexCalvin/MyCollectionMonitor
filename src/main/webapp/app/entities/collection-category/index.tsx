import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CollectionCategory from './collection-category';
import CollectionCategoryDetail from './collection-category-detail';
import CollectionCategoryUpdate from './collection-category-update';
import CollectionCategoryDeleteDialog from './collection-category-delete-dialog';

const CollectionCategoryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CollectionCategory />} />
    <Route path="new" element={<CollectionCategoryUpdate />} />
    <Route path=":id">
      <Route index element={<CollectionCategoryDetail />} />
      <Route path="edit" element={<CollectionCategoryUpdate />} />
      <Route path="delete" element={<CollectionCategoryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CollectionCategoryRoutes;
