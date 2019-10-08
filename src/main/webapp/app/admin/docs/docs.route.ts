import { Route } from '@angular/router';

import { KoDocsComponent } from './docs.component';

export const docsRoute: Route = {
  path: 'docs',
  component: KoDocsComponent,
  data: {
    pageTitle: 'global.menu.admin.apidocs'
  }
};
