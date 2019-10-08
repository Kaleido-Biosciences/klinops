import { Route } from '@angular/router';

import { KoHealthCheckComponent } from './health.component';

export const healthRoute: Route = {
  path: 'health',
  component: KoHealthCheckComponent,
  data: {
    pageTitle: 'health.title'
  }
};
