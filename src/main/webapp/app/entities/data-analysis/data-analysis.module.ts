/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KlinopsSharedModule } from 'app/shared/shared.module';
import { DataAnalysisComponent } from './data-analysis.component';
import { DataAnalysisDetailComponent } from './data-analysis-detail.component';
import { DataAnalysisUpdateComponent } from './data-analysis-update.component';
import { DataAnalysisDeletePopupComponent, DataAnalysisDeleteDialogComponent } from './data-analysis-delete-dialog.component';
import { dataAnalysisRoute, dataAnalysisPopupRoute } from './data-analysis.route';

const ENTITY_STATES = [...dataAnalysisRoute, ...dataAnalysisPopupRoute];

@NgModule({
  imports: [KlinopsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    DataAnalysisComponent,
    DataAnalysisDetailComponent,
    DataAnalysisUpdateComponent,
    DataAnalysisDeleteDialogComponent,
    DataAnalysisDeletePopupComponent
  ],
  entryComponents: [DataAnalysisDeleteDialogComponent]
})
export class KlinopsDataAnalysisModule {}
