import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KlinopsSharedModule } from 'app/shared/shared.module';
import { LaboratoryComponent } from './laboratory.component';
import { LaboratoryDetailComponent } from './laboratory-detail.component';
import { LaboratoryUpdateComponent } from './laboratory-update.component';
import { LaboratoryDeletePopupComponent, LaboratoryDeleteDialogComponent } from './laboratory-delete-dialog.component';
import { laboratoryRoute, laboratoryPopupRoute } from './laboratory.route';

const ENTITY_STATES = [...laboratoryRoute, ...laboratoryPopupRoute];

@NgModule({
  imports: [KlinopsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    LaboratoryComponent,
    LaboratoryDetailComponent,
    LaboratoryUpdateComponent,
    LaboratoryDeleteDialogComponent,
    LaboratoryDeletePopupComponent
  ],
  entryComponents: [LaboratoryDeleteDialogComponent]
})
export class KlinopsLaboratoryModule {}
