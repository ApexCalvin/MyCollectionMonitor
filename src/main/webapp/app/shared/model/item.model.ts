import dayjs from 'dayjs';
import { ICollectionCategory } from 'app/shared/model/collection-category.model';

export interface IItem {
  id?: number;
  name?: string | null;
  releaseDate?: string | null;
  releasePrice?: number | null;
  purchaseDate?: string | null;
  purchasePrice?: number | null;
  marketPrice?: number | null;
  collectionCategory?: ICollectionCategory | null;
}

export const defaultValue: Readonly<IItem> = {};
