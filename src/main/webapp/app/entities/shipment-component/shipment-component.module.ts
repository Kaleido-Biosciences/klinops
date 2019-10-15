/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KlinopsSharedModule } from 'app/shared/shared.module';
import { ShipmentComponentComponent } from './shipment-component.component';
import { ShipmentComponentDetailComponent } from './shipment-component-detail.component';
import { ShipmentComponentUpdateComponent } from './shipment-component-update.component';
import {
  ShipmentComponentDeletePopupComponent,
  ShipmentComponentDeleteDialogComponent
} from './shipment-component-delete-dialog.component';
import { shipmentComponentRoute, shipmentComponentPopupRoute } from './shipment-component.route';

const ENTITY_STATES = [...shipmentComponentRoute, ...shipmentComponentPopupRoute];

@NgModule({
  imports: [KlinopsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ShipmentComponentComponent,
    ShipmentComponentDetailComponent,
    ShipmentComponentUpdateComponent,
    ShipmentComponentDeleteDialogComponent,
    ShipmentComponentDeletePopupComponent
  ],
  entryComponents: [ShipmentComponentDeleteDialogComponent]
})
export class KlinopsShipmentComponentModule {}
