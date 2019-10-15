/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KlinopsSharedModule } from 'app/shared/shared.module';
import { StudyMilestoneComponent } from './study-milestone.component';
import { StudyMilestoneDetailComponent } from './study-milestone-detail.component';
import { StudyMilestoneUpdateComponent } from './study-milestone-update.component';
import { StudyMilestoneDeletePopupComponent, StudyMilestoneDeleteDialogComponent } from './study-milestone-delete-dialog.component';
import { studyMilestoneRoute, studyMilestonePopupRoute } from './study-milestone.route';

const ENTITY_STATES = [...studyMilestoneRoute, ...studyMilestonePopupRoute];

@NgModule({
  imports: [KlinopsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    StudyMilestoneComponent,
    StudyMilestoneDetailComponent,
    StudyMilestoneUpdateComponent,
    StudyMilestoneDeleteDialogComponent,
    StudyMilestoneDeletePopupComponent
  ],
  entryComponents: [StudyMilestoneDeleteDialogComponent]
})
export class KlinopsStudyMilestoneModule {}
