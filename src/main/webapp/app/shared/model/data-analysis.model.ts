import { Moment } from 'moment';
import { IBioAnalysis } from 'app/shared/model/bio-analysis.model';
import { IClinicalStudy } from 'app/shared/model/clinical-study.model';

export interface IDataAnalysis {
  id?: number;
  dataAnalysesType?: string;
  contactName?: string;
  contactEmail?: string;
  anticipatedAnalysisDeliveryDate?: Moment;
  actualAnalysisDeliveryDate?: Moment;
  dataLocation?: string;
  bioAnalyses?: IBioAnalysis[];
  study?: IClinicalStudy;
}

export class DataAnalysis implements IDataAnalysis {
  constructor(
    public id?: number,
    public dataAnalysesType?: string,
    public contactName?: string,
    public contactEmail?: string,
    public anticipatedAnalysisDeliveryDate?: Moment,
    public actualAnalysisDeliveryDate?: Moment,
    public dataLocation?: string,
    public bioAnalyses?: IBioAnalysis[],
    public study?: IClinicalStudy
  ) {}
}
