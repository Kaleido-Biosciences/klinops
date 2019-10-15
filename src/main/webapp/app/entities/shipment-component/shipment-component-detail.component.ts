/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IShipmentComponent } from 'app/shared/model/shipment-component.model';

@Component({
  selector: 'ko-shipment-component-detail',
  templateUrl: './shipment-component-detail.component.html'
})
export class ShipmentComponentDetailComponent implements OnInit {
  shipmentComponent: IShipmentComponent;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ shipmentComponent }) => {
      this.shipmentComponent = shipmentComponent;
    });
  }

  previousState() {
    window.history.back();
  }
}
