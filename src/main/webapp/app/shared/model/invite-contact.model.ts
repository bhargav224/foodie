import { IUserInfo } from 'app/shared/model/user-info.model';

export interface IInviteContact {
  id?: number;
  contact?: number;
  userInfo?: IUserInfo;
}

export const defaultValue: Readonly<IInviteContact> = {};
