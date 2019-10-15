/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie';

@Injectable({ providedIn: 'root' })
export class CSRFService {
  constructor(private cookieService: CookieService) {}

  getCSRF(name = 'XSRF-TOKEN') {
    return this.cookieService.get(name);
  }
}
