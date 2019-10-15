/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { SpyObject } from './spyobject';
import { JhiAlertService, JhiAlert } from 'ng-jhipster';

export class MockAlertService extends SpyObject {
  constructor() {
    super(JhiAlertService);
  }
  addAlert(alertOptions: JhiAlert) {
    return alertOptions;
  }
}
