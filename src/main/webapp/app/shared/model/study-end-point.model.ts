import { IClinicalStudy } from 'app/shared/model/clinical-study.model';

export interface IStudyEndPoint {
  id?: number;
  description?: string;
  objective?: string;
  endPointType?: string;
  study?: IClinicalStudy;
}

export class StudyEndPoint implements IStudyEndPoint {
  constructor(
    public id?: number,
    public description?: string,
    public objective?: string,
    public endPointType?: string,
    public study?: IClinicalStudy
  ) {}
}
