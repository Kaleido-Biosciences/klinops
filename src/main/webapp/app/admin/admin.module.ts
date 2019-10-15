/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { KlinopsSharedModule } from 'app/shared/shared.module';
/* jhipster-needle-add-admin-module-import - JHipster will add admin modules imports here */

import { adminState } from './admin.route';
import { AuditsComponent } from './audits/audits.component';
import { LogsComponent } from './logs/logs.component';
import { KoMetricsMonitoringComponent } from './metrics/metrics.component';
import { KoHealthModalComponent } from './health/health-modal.component';
import { KoHealthCheckComponent } from './health/health.component';
import { KoConfigurationComponent } from './configuration/configuration.component';
import { KoDocsComponent } from './docs/docs.component';

@NgModule({
  imports: [
    KlinopsSharedModule,
    /* jhipster-needle-add-admin-module - JHipster will add admin modules here */
    RouterModule.forChild(adminState)
  ],
  declarations: [
    AuditsComponent,
    LogsComponent,
    KoConfigurationComponent,
    KoHealthCheckComponent,
    KoHealthModalComponent,
    KoDocsComponent,
    KoMetricsMonitoringComponent
  ],
  entryComponents: [KoHealthModalComponent]
})
export class KlinopsAdminModule {}
