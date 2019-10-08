import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { ILaboratory, Laboratory } from 'app/shared/model/laboratory.model';
import { LaboratoryService } from './laboratory.service';

@Component({
  selector: 'ko-laboratory-update',
  templateUrl: './laboratory-update.component.html'
})
export class LaboratoryUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    labName: [null, [Validators.required]],
    streetAddress: [null, [Validators.required]],
    city: [null, [Validators.required]],
    state: [],
    zip: [null, [Validators.required]],
    country: [null, [Validators.required]],
    labContactEmail: [null, [Validators.pattern('^(.+)@(.+)|$')]],
    labContactName: [],
    labContactPhoneNumber: []
  });

  constructor(protected laboratoryService: LaboratoryService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ laboratory }) => {
      this.updateForm(laboratory);
    });
  }

  updateForm(laboratory: ILaboratory) {
    this.editForm.patchValue({
      id: laboratory.id,
      labName: laboratory.labName,
      streetAddress: laboratory.streetAddress,
      city: laboratory.city,
      state: laboratory.state,
      zip: laboratory.zip,
      country: laboratory.country,
      labContactEmail: laboratory.labContactEmail,
      labContactName: laboratory.labContactName,
      labContactPhoneNumber: laboratory.labContactPhoneNumber
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const laboratory = this.createFromForm();
    if (laboratory.id !== undefined) {
      this.subscribeToSaveResponse(this.laboratoryService.update(laboratory));
    } else {
      this.subscribeToSaveResponse(this.laboratoryService.create(laboratory));
    }
  }

  private createFromForm(): ILaboratory {
    return {
      ...new Laboratory(),
      id: this.editForm.get(['id']).value,
      labName: this.editForm.get(['labName']).value,
      streetAddress: this.editForm.get(['streetAddress']).value,
      city: this.editForm.get(['city']).value,
      state: this.editForm.get(['state']).value,
      zip: this.editForm.get(['zip']).value,
      country: this.editForm.get(['country']).value,
      labContactEmail: this.editForm.get(['labContactEmail']).value,
      labContactName: this.editForm.get(['labContactName']).value,
      labContactPhoneNumber: this.editForm.get(['labContactPhoneNumber']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILaboratory>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
