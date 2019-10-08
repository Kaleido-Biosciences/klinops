import { IClinicalStudy } from 'app/shared/model/clinical-study.model';

export interface IStudyProduct {
  id?: number;
  productName?: string;
  doseRange?: string;
  daysOfExposure?: number;
  formulation?: string;
  study?: IClinicalStudy;
}

export class StudyProduct implements IStudyProduct {
  constructor(
    public id?: number,
    public productName?: string,
    public doseRange?: string,
    public daysOfExposure?: number,
    public formulation?: string,
    public study?: IClinicalStudy
  ) {}
}
