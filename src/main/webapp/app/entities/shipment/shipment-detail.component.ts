/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IShipment } from 'app/shared/model/shipment.model';

@Component({
  selector: 'ko-shipment-detail',
  templateUrl: './shipment-detail.component.html'
})
export class ShipmentDetailComponent implements OnInit {
  shipment: IShipment;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ shipment }) => {
      this.shipment = shipment;
    });
  }

  previousState() {
    window.history.back();
  }
}
