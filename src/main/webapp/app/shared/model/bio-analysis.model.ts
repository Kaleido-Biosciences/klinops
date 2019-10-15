/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { Moment } from 'moment';
import { IStudyEndPoint } from 'app/shared/model/study-end-point.model';
import { ILaboratory } from 'app/shared/model/laboratory.model';
import { IClinicalStudy } from 'app/shared/model/clinical-study.model';
import { IDataAnalysis } from 'app/shared/model/data-analysis.model';

export interface IBioAnalysis {
  id?: number;
  analyte?: string;
  sampleType?: string;
  bioAnalysisType?: string;
  anticipatedLabWorkStartDate?: Moment;
  actualLabWorkStartDate?: Moment;
  anticipatedLabResultDeliveryDate?: Moment;
  actualLabResultDeliveryDate?: Moment;
  dataLocation?: string;
  contactName?: string;
  contactEmail?: string;
  comments?: string;
  studyEndPoint?: IStudyEndPoint;
  laboratories?: ILaboratory;
  study?: IClinicalStudy;
  dataAnalyses?: IDataAnalysis[];
}

export class BioAnalysis implements IBioAnalysis {
  constructor(
    public id?: number,
    public analyte?: string,
    public sampleType?: string,
    public bioAnalysisType?: string,
    public anticipatedLabWorkStartDate?: Moment,
    public actualLabWorkStartDate?: Moment,
    public anticipatedLabResultDeliveryDate?: Moment,
    public actualLabResultDeliveryDate?: Moment,
    public dataLocation?: string,
    public contactName?: string,
    public contactEmail?: string,
    public comments?: string,
    public studyEndPoint?: IStudyEndPoint,
    public laboratories?: ILaboratory,
    public study?: IClinicalStudy,
    public dataAnalyses?: IDataAnalysis[]
  ) {}
}
