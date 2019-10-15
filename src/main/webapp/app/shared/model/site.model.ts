/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { IPrincipalInvestigator } from 'app/shared/model/principal-investigator.model';

export interface ISite {
  id?: number;
  siteName?: string;
  institution?: string;
  streetAddress?: string;
  city?: string;
  state?: string;
  zip?: string;
  country?: string;
  investigators?: IPrincipalInvestigator[];
}

export class Site implements ISite {
  constructor(
    public id?: number,
    public siteName?: string,
    public institution?: string,
    public streetAddress?: string,
    public city?: string,
    public state?: string,
    public zip?: string,
    public country?: string,
    public investigators?: IPrincipalInvestigator[]
  ) {}
}
