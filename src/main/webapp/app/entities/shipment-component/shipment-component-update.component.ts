/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IShipmentComponent, ShipmentComponent } from 'app/shared/model/shipment-component.model';
import { ShipmentComponentService } from './shipment-component.service';
import { IShipment } from 'app/shared/model/shipment.model';
import { ShipmentService } from 'app/entities/shipment/shipment.service';

@Component({
  selector: 'ko-shipment-component-update',
  templateUrl: './shipment-component-update.component.html'
})
export class ShipmentComponentUpdateComponent implements OnInit {
  isSaving: boolean;

  shipments: IShipment[];

  editForm = this.fb.group({
    id: [],
    sampleType: [null, [Validators.required]],
    sampleCount: [null, [Validators.required, Validators.min(1)]],
    shipment: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected shipmentComponentService: ShipmentComponentService,
    protected shipmentService: ShipmentService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ shipmentComponent }) => {
      this.updateForm(shipmentComponent);
    });
    this.shipmentService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IShipment[]>) => mayBeOk.ok),
        map((response: HttpResponse<IShipment[]>) => response.body)
      )
      .subscribe((res: IShipment[]) => (this.shipments = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(shipmentComponent: IShipmentComponent) {
    this.editForm.patchValue({
      id: shipmentComponent.id,
      sampleType: shipmentComponent.sampleType,
      sampleCount: shipmentComponent.sampleCount,
      shipment: shipmentComponent.shipment
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const shipmentComponent = this.createFromForm();
    if (shipmentComponent.id !== undefined) {
      this.subscribeToSaveResponse(this.shipmentComponentService.update(shipmentComponent));
    } else {
      this.subscribeToSaveResponse(this.shipmentComponentService.create(shipmentComponent));
    }
  }

  private createFromForm(): IShipmentComponent {
    return {
      ...new ShipmentComponent(),
      id: this.editForm.get(['id']).value,
      sampleType: this.editForm.get(['sampleType']).value,
      sampleCount: this.editForm.get(['sampleCount']).value,
      shipment: this.editForm.get(['shipment']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IShipmentComponent>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackShipmentById(index: number, item: IShipment) {
    return item.id;
  }
}
