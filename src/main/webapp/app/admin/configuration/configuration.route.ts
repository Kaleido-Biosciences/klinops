import { Route } from '@angular/router';

import { KoConfigurationComponent } from './configuration.component';

export const configurationRoute: Route = {
  path: 'configuration',
  component: KoConfigurationComponent,
  data: {
    pageTitle: 'configuration.title'
  }
};
