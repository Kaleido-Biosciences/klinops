import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KlinopsSharedModule } from 'app/shared/shared.module';
import { PrincipalInvestigatorComponent } from './principal-investigator.component';
import { PrincipalInvestigatorDetailComponent } from './principal-investigator-detail.component';
import { PrincipalInvestigatorUpdateComponent } from './principal-investigator-update.component';
import {
  PrincipalInvestigatorDeletePopupComponent,
  PrincipalInvestigatorDeleteDialogComponent
} from './principal-investigator-delete-dialog.component';
import { principalInvestigatorRoute, principalInvestigatorPopupRoute } from './principal-investigator.route';

const ENTITY_STATES = [...principalInvestigatorRoute, ...principalInvestigatorPopupRoute];

@NgModule({
  imports: [KlinopsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    PrincipalInvestigatorComponent,
    PrincipalInvestigatorDetailComponent,
    PrincipalInvestigatorUpdateComponent,
    PrincipalInvestigatorDeleteDialogComponent,
    PrincipalInvestigatorDeletePopupComponent
  ],
  entryComponents: [PrincipalInvestigatorDeleteDialogComponent]
})
export class KlinopsPrincipalInvestigatorModule {}
