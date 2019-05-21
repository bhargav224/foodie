import { IUserInfoRole } from 'app/shared/model/user-info-role.model';

export interface IRole {
  id?: number;
  name?: string;
  userInfoRoles?: IUserInfoRole[];
}

export const defaultValue: Readonly<IRole> = {};
