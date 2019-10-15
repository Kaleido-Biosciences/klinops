/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { ITrialMasterFile } from 'app/shared/model/trial-master-file.model';
import { IStudyEndPoint } from 'app/shared/model/study-end-point.model';
import { IStudyProduct } from 'app/shared/model/study-product.model';
import { IStudyMilestone } from 'app/shared/model/study-milestone.model';
import { IBioAnalysis } from 'app/shared/model/bio-analysis.model';
import { IDataAnalysis } from 'app/shared/model/data-analysis.model';
import { IShipment } from 'app/shared/model/shipment.model';
import { IStudySample } from 'app/shared/model/study-sample.model';
import { IPrincipalInvestigator } from 'app/shared/model/principal-investigator.model';

export interface IClinicalStudy {
  id?: number;
  studyIdentifier?: string;
  phase?: string;
  status?: string;
  sequence?: number;
  studyYear?: number;
  name?: string;
  design?: string;
  numberOfCohorts?: number;
  intendedSubjectsPerCohort?: number;
  populationDiseaseState?: string;
  minimumAge?: number;
  maximumAge?: number;
  subjectsEnrolled?: number;
  femalesEligible?: boolean;
  malesEligible?: boolean;
  studyShortName?: string;
  projectManager?: string;
  principalPhysician?: string;
  researchRepresentative?: string;
  analysisRepresentative?: string;
  dataManager?: string;
  masterFile?: ITrialMasterFile;
  endPoints?: IStudyEndPoint[];
  studiedProducts?: IStudyProduct[];
  mileStones?: IStudyMilestone[];
  bioAnalyses?: IBioAnalysis[];
  dataAnalyses?: IDataAnalysis[];
  shipments?: IShipment[];
  studySamples?: IStudySample[];
  investigators?: IPrincipalInvestigator[];
}

export class ClinicalStudy implements IClinicalStudy {
  constructor(
    public id?: number,
    public studyIdentifier?: string,
    public phase?: string,
    public status?: string,
    public sequence?: number,
    public studyYear?: number,
    public name?: string,
    public design?: string,
    public numberOfCohorts?: number,
    public intendedSubjectsPerCohort?: number,
    public populationDiseaseState?: string,
    public minimumAge?: number,
    public maximumAge?: number,
    public subjectsEnrolled?: number,
    public femalesEligible?: boolean,
    public malesEligible?: boolean,
    public studyShortName?: string,
    public projectManager?: string,
    public principalPhysician?: string,
    public researchRepresentative?: string,
    public analysisRepresentative?: string,
    public dataManager?: string,
    public masterFile?: ITrialMasterFile,
    public endPoints?: IStudyEndPoint[],
    public studiedProducts?: IStudyProduct[],
    public mileStones?: IStudyMilestone[],
    public bioAnalyses?: IBioAnalysis[],
    public dataAnalyses?: IDataAnalysis[],
    public shipments?: IShipment[],
    public studySamples?: IStudySample[],
    public investigators?: IPrincipalInvestigator[]
  ) {
    this.femalesEligible = this.femalesEligible || false;
    this.malesEligible = this.malesEligible || false;
  }
}
