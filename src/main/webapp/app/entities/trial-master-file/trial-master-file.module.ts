import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KlinopsSharedModule } from 'app/shared/shared.module';
import { TrialMasterFileComponent } from './trial-master-file.component';
import { TrialMasterFileDetailComponent } from './trial-master-file-detail.component';
import { TrialMasterFileUpdateComponent } from './trial-master-file-update.component';
import { TrialMasterFileDeletePopupComponent, TrialMasterFileDeleteDialogComponent } from './trial-master-file-delete-dialog.component';
import { trialMasterFileRoute, trialMasterFilePopupRoute } from './trial-master-file.route';

const ENTITY_STATES = [...trialMasterFileRoute, ...trialMasterFilePopupRoute];

@NgModule({
  imports: [KlinopsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    TrialMasterFileComponent,
    TrialMasterFileDetailComponent,
    TrialMasterFileUpdateComponent,
    TrialMasterFileDeleteDialogComponent,
    TrialMasterFileDeletePopupComponent
  ],
  entryComponents: [TrialMasterFileDeleteDialogComponent]
})
export class KlinopsTrialMasterFileModule {}
