/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KlinopsSharedModule } from 'app/shared/shared.module';
import { StudyEndPointComponent } from './study-end-point.component';
import { StudyEndPointDetailComponent } from './study-end-point-detail.component';
import { StudyEndPointUpdateComponent } from './study-end-point-update.component';
import { StudyEndPointDeletePopupComponent, StudyEndPointDeleteDialogComponent } from './study-end-point-delete-dialog.component';
import { studyEndPointRoute, studyEndPointPopupRoute } from './study-end-point.route';

const ENTITY_STATES = [...studyEndPointRoute, ...studyEndPointPopupRoute];

@NgModule({
  imports: [KlinopsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    StudyEndPointComponent,
    StudyEndPointDetailComponent,
    StudyEndPointUpdateComponent,
    StudyEndPointDeleteDialogComponent,
    StudyEndPointDeletePopupComponent
  ],
  entryComponents: [StudyEndPointDeleteDialogComponent]
})
export class KlinopsStudyEndPointModule {}
