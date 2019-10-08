import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KlinopsSharedModule } from 'app/shared/shared.module';
import { StudySampleComponent } from './study-sample.component';
import { StudySampleDetailComponent } from './study-sample-detail.component';
import { StudySampleUpdateComponent } from './study-sample-update.component';
import { StudySampleDeletePopupComponent, StudySampleDeleteDialogComponent } from './study-sample-delete-dialog.component';
import { studySampleRoute, studySamplePopupRoute } from './study-sample.route';

const ENTITY_STATES = [...studySampleRoute, ...studySamplePopupRoute];

@NgModule({
  imports: [KlinopsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    StudySampleComponent,
    StudySampleDetailComponent,
    StudySampleUpdateComponent,
    StudySampleDeleteDialogComponent,
    StudySampleDeletePopupComponent
  ],
  entryComponents: [StudySampleDeleteDialogComponent]
})
export class KlinopsStudySampleModule {}
