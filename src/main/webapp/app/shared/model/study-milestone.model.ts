import { Moment } from 'moment';
import { IClinicalStudy } from 'app/shared/model/clinical-study.model';

export interface IStudyMilestone {
  id?: number;
  mileStoneName?: string;
  mileStoneType?: string;
  projectedCompletionDate?: Moment;
  actualCompletionDate?: Moment;
  study?: IClinicalStudy;
}

export class StudyMilestone implements IStudyMilestone {
  constructor(
    public id?: number,
    public mileStoneName?: string,
    public mileStoneType?: string,
    public projectedCompletionDate?: Moment,
    public actualCompletionDate?: Moment,
    public study?: IClinicalStudy
  ) {}
}
