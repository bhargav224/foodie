import { IUserInfo } from 'app/shared/model/user-info.model';

export interface IInviteEmail {
  id?: number;
  email?: string;
  userInfo?: IUserInfo;
}

export const defaultValue: Readonly<IInviteEmail> = {};
