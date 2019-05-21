import { IRecipeHasStep } from 'app/shared/model/recipe-has-step.model';

export interface IRecipeStep {
  id?: number;
  instruction?: string;
  name?: string;
  path?: string;
  recipeHasSteps?: IRecipeHasStep[];
}

export const defaultValue: Readonly<IRecipeStep> = {};
