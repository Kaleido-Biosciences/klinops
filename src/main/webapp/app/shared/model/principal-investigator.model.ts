/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { ISite } from 'app/shared/model/site.model';
import { IClinicalStudy } from 'app/shared/model/clinical-study.model';

export interface IPrincipalInvestigator {
  id?: number;
  investigatorName?: string;
  streetAddress?: string;
  city?: string;
  state?: string;
  zip?: string;
  country?: string;
  email?: string;
  phoneNumber?: string;
  site?: ISite;
  studies?: IClinicalStudy[];
}

export class PrincipalInvestigator implements IPrincipalInvestigator {
  constructor(
    public id?: number,
    public investigatorName?: string,
    public streetAddress?: string,
    public city?: string,
    public state?: string,
    public zip?: string,
    public country?: string,
    public email?: string,
    public phoneNumber?: string,
    public site?: ISite,
    public studies?: IClinicalStudy[]
  ) {}
}
