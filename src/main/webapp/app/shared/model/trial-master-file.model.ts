/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { IClinicalStudy } from 'app/shared/model/clinical-study.model';

export interface ITrialMasterFile {
  id?: number;
  fileName?: string;
  location?: string;
  status?: string;
  electronic?: boolean;
  clinicalStudy?: IClinicalStudy;
}

export class TrialMasterFile implements ITrialMasterFile {
  constructor(
    public id?: number,
    public fileName?: string,
    public location?: string,
    public status?: string,
    public electronic?: boolean,
    public clinicalStudy?: IClinicalStudy
  ) {
    this.electronic = this.electronic || false;
  }
}
