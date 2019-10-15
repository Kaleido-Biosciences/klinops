/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KlinopsSharedModule } from 'app/shared/shared.module';
import { StudyProductComponent } from './study-product.component';
import { StudyProductDetailComponent } from './study-product-detail.component';
import { StudyProductUpdateComponent } from './study-product-update.component';
import { StudyProductDeletePopupComponent, StudyProductDeleteDialogComponent } from './study-product-delete-dialog.component';
import { studyProductRoute, studyProductPopupRoute } from './study-product.route';

const ENTITY_STATES = [...studyProductRoute, ...studyProductPopupRoute];

@NgModule({
  imports: [KlinopsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    StudyProductComponent,
    StudyProductDetailComponent,
    StudyProductUpdateComponent,
    StudyProductDeleteDialogComponent,
    StudyProductDeletePopupComponent
  ],
  entryComponents: [StudyProductDeleteDialogComponent]
})
export class KlinopsStudyProductModule {}
