import { IUserInfo } from 'app/shared/model/user-info.model';

export interface IChefProfile {
  id?: number;
  photo?: string;
  speciality?: string;
  type?: string;
  website?: string;
  userInfo?: IUserInfo;
}

export const defaultValue: Readonly<IChefProfile> = {};
