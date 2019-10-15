/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KlinopsSharedModule } from 'app/shared/shared.module';
import { BioAnalysisComponent } from './bio-analysis.component';
import { BioAnalysisDetailComponent } from './bio-analysis-detail.component';
import { BioAnalysisUpdateComponent } from './bio-analysis-update.component';
import { BioAnalysisDeletePopupComponent, BioAnalysisDeleteDialogComponent } from './bio-analysis-delete-dialog.component';
import { bioAnalysisRoute, bioAnalysisPopupRoute } from './bio-analysis.route';

const ENTITY_STATES = [...bioAnalysisRoute, ...bioAnalysisPopupRoute];

@NgModule({
  imports: [KlinopsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    BioAnalysisComponent,
    BioAnalysisDetailComponent,
    BioAnalysisUpdateComponent,
    BioAnalysisDeleteDialogComponent,
    BioAnalysisDeletePopupComponent
  ],
  entryComponents: [BioAnalysisDeleteDialogComponent]
})
export class KlinopsBioAnalysisModule {}
