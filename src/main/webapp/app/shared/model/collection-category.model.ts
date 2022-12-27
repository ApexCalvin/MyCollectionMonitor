import { IUser } from 'app/shared/model/user.model';
import { IItem } from 'app/shared/model/item.model';

export interface ICollectionCategory {
  id?: number;
  categoryName?: string | null;
  user?: IUser | null;
  items?: IItem[] | null;
}

export const defaultValue: Readonly<ICollectionCategory> = {};
