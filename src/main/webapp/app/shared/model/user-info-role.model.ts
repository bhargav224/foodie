import { IRole } from 'app/shared/model/role.model';
import { IUserInfo } from 'app/shared/model/user-info.model';

export interface IUserInfoRole {
  id?: number;
  role?: IRole;
  userInfo?: IUserInfo;
}

export const defaultValue: Readonly<IUserInfoRole> = {};
