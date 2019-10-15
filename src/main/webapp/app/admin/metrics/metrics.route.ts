/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { Route } from '@angular/router';

import { KoMetricsMonitoringComponent } from './metrics.component';

export const metricsRoute: Route = {
  path: 'metrics',
  component: KoMetricsMonitoringComponent,
  data: {
    pageTitle: 'metrics.title'
  }
};
