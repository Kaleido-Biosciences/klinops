/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { Route } from '@angular/router';

import { KoHealthCheckComponent } from './health.component';

export const healthRoute: Route = {
  path: 'health',
  component: KoHealthCheckComponent,
  data: {
    pageTitle: 'health.title'
  }
};
