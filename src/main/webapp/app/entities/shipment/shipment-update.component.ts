import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { IShipment, Shipment } from 'app/shared/model/shipment.model';
import { ShipmentService } from './shipment.service';
import { ILaboratory } from 'app/shared/model/laboratory.model';
import { LaboratoryService } from 'app/entities/laboratory/laboratory.service';
import { IClinicalStudy } from 'app/shared/model/clinical-study.model';
import { ClinicalStudyService } from 'app/entities/clinical-study/clinical-study.service';

@Component({
  selector: 'ko-shipment-update',
  templateUrl: './shipment-update.component.html'
})
export class ShipmentUpdateComponent implements OnInit {
  isSaving: boolean;

  laboratories: ILaboratory[];

  clinicalstudies: IClinicalStudy[];
  dateShippedDp: any;
  dateReceivedDp: any;

  editForm = this.fb.group({
    id: [],
    shipmentCode: [null, [Validators.required]],
    dateShipped: [null, [Validators.required]],
    dateReceived: [],
    destination: [null, Validators.required],
    study: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected shipmentService: ShipmentService,
    protected laboratoryService: LaboratoryService,
    protected clinicalStudyService: ClinicalStudyService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ shipment }) => {
      this.updateForm(shipment);
    });
    this.laboratoryService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ILaboratory[]>) => mayBeOk.ok),
        map((response: HttpResponse<ILaboratory[]>) => response.body)
      )
      .subscribe((res: ILaboratory[]) => (this.laboratories = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.clinicalStudyService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IClinicalStudy[]>) => mayBeOk.ok),
        map((response: HttpResponse<IClinicalStudy[]>) => response.body)
      )
      .subscribe((res: IClinicalStudy[]) => (this.clinicalstudies = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(shipment: IShipment) {
    this.editForm.patchValue({
      id: shipment.id,
      shipmentCode: shipment.shipmentCode,
      dateShipped: shipment.dateShipped,
      dateReceived: shipment.dateReceived,
      destination: shipment.destination,
      study: shipment.study
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const shipment = this.createFromForm();
    if (shipment.id !== undefined) {
      this.subscribeToSaveResponse(this.shipmentService.update(shipment));
    } else {
      this.subscribeToSaveResponse(this.shipmentService.create(shipment));
    }
  }

  private createFromForm(): IShipment {
    return {
      ...new Shipment(),
      id: this.editForm.get(['id']).value,
      shipmentCode: this.editForm.get(['shipmentCode']).value,
      dateShipped: this.editForm.get(['dateShipped']).value,
      dateReceived: this.editForm.get(['dateReceived']).value,
      destination: this.editForm.get(['destination']).value,
      study: this.editForm.get(['study']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IShipment>>) {
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

  trackLaboratoryById(index: number, item: ILaboratory) {
    return item.id;
  }

  trackClinicalStudyById(index: number, item: IClinicalStudy) {
    return item.id;
  }
}
