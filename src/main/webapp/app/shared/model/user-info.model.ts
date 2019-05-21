import { Moment } from 'moment';
import { IUserInfo } from 'app/shared/model/user-info.model';
import { IChefProfile } from 'app/shared/model/chef-profile.model';
import { IUserInfoRole } from 'app/shared/model/user-info-role.model';
import { IRecipeComment } from 'app/shared/model/recipe-comment.model';
import { IRecipeRating } from 'app/shared/model/recipe-rating.model';
import { IRecipeLike } from 'app/shared/model/recipe-like.model';
import { IFootnote } from 'app/shared/model/footnote.model';
import { IShareRecipe } from 'app/shared/model/share-recipe.model';
import { IInviteEmail } from 'app/shared/model/invite-email.model';
import { IInviteContact } from 'app/shared/model/invite-contact.model';
import { IUserRating } from 'app/shared/model/user-rating.model';
import { IRestaurant } from 'app/shared/model/restaurant.model';

export interface IUserInfo {
  id?: number;
  authenticated?: boolean;
  contact?: number;
  currentLoggedIn?: Moment;
  email?: string;
  emailConfirmationSentOn?: Moment;
  emailConfirmed?: boolean;
  emailConfirmedOn?: Moment;
  lastLoggedIn?: Moment;
  firstName?: string;
  lastName?: string;
  password?: string;
  photo?: string;
  registeredOn?: Moment;
  userInfo?: IUserInfo;
  chefProfile?: IChefProfile;
  userInfoRoles?: IUserInfoRole[];
  recipeComments?: IRecipeComment[];
  recipeRatings?: IRecipeRating[];
  recipeLikes?: IRecipeLike[];
  footnotes?: IFootnote[];
  forSharedBies?: IShareRecipe[];
  forSharedTos?: IShareRecipe[];
  inviteEmails?: IInviteEmail[];
  inviteContacts?: IInviteContact[];
  forRateByUsers?: IUserRating[];
  forRateToUsers?: IUserRating[];
  invitedBy?: IUserInfo;
  restaurant?: IRestaurant;
}

export const defaultValue: Readonly<IUserInfo> = {
  authenticated: false,
  emailConfirmed: false
};
