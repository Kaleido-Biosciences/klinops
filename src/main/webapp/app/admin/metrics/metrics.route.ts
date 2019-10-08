import { Route } from '@angular/router';

import { KoMetricsMonitoringComponent } from './metrics.component';

export const metricsRoute: Route = {
  path: 'metrics',
  component: KoMetricsMonitoringComponent,
  data: {
    pageTitle: 'metrics.title'
  }
};
