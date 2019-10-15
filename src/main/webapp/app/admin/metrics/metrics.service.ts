/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class KoMetricsService {
  constructor(private http: HttpClient) {}

  getMetrics(): Observable<any> {
    return this.http.get(SERVER_API_URL + 'management/jhimetrics');
  }

  threadDump(): Observable<any> {
    return this.http.get(SERVER_API_URL + 'management/threaddump');
  }
}
