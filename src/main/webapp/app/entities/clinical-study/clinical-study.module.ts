/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KlinopsSharedModule } from 'app/shared/shared.module';
import { ClinicalStudyComponent } from './clinical-study.component';
import { ClinicalStudyDetailComponent } from './clinical-study-detail.component';
import { ClinicalStudyUpdateComponent } from './clinical-study-update.component';
import { ClinicalStudyDeletePopupComponent, ClinicalStudyDeleteDialogComponent } from './clinical-study-delete-dialog.component';
import { clinicalStudyRoute, clinicalStudyPopupRoute } from './clinical-study.route';

const ENTITY_STATES = [...clinicalStudyRoute, ...clinicalStudyPopupRoute];

@NgModule({
  imports: [KlinopsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ClinicalStudyComponent,
    ClinicalStudyDetailComponent,
    ClinicalStudyUpdateComponent,
    ClinicalStudyDeleteDialogComponent,
    ClinicalStudyDeletePopupComponent
  ],
  entryComponents: [ClinicalStudyDeleteDialogComponent]
})
export class KlinopsClinicalStudyModule {}
