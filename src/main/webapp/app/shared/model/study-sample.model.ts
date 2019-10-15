/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { IClinicalStudy } from 'app/shared/model/clinical-study.model';

export interface IStudySample {
  id?: number;
  sampleType?: string;
  expectedNumberOfSamples?: number;
  study?: IClinicalStudy;
}

export class StudySample implements IStudySample {
  constructor(public id?: number, public sampleType?: string, public expectedNumberOfSamples?: number, public study?: IClinicalStudy) {}
}
