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
import { IStudyProduct, StudyProduct } from 'app/shared/model/study-product.model';
import { StudyProductService } from './study-product.service';
import { IClinicalStudy } from 'app/shared/model/clinical-study.model';
import { ClinicalStudyService } from 'app/entities/clinical-study/clinical-study.service';

@Component({
  selector: 'ko-study-product-update',
  templateUrl: './study-product-update.component.html'
})
export class StudyProductUpdateComponent implements OnInit {
  isSaving: boolean;

  clinicalstudies: IClinicalStudy[];

  editForm = this.fb.group({
    id: [],
    productName: [null, [Validators.required]],
    doseRange: [],
    daysOfExposure: [null, [Validators.min(0)]],
    formulation: [null, [Validators.required]],
    study: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected studyProductService: StudyProductService,
    protected clinicalStudyService: ClinicalStudyService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ studyProduct }) => {
      this.updateForm(studyProduct);
    });
    this.clinicalStudyService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IClinicalStudy[]>) => mayBeOk.ok),
        map((response: HttpResponse<IClinicalStudy[]>) => response.body)
      )
      .subscribe((res: IClinicalStudy[]) => (this.clinicalstudies = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(studyProduct: IStudyProduct) {
    this.editForm.patchValue({
      id: studyProduct.id,
      productName: studyProduct.productName,
      doseRange: studyProduct.doseRange,
      daysOfExposure: studyProduct.daysOfExposure,
      formulation: studyProduct.formulation,
      study: studyProduct.study
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const studyProduct = this.createFromForm();
    if (studyProduct.id !== undefined) {
      this.subscribeToSaveResponse(this.studyProductService.update(studyProduct));
    } else {
      this.subscribeToSaveResponse(this.studyProductService.create(studyProduct));
    }
  }

  private createFromForm(): IStudyProduct {
    return {
      ...new StudyProduct(),
      id: this.editForm.get(['id']).value,
      productName: this.editForm.get(['productName']).value,
      doseRange: this.editForm.get(['doseRange']).value,
      daysOfExposure: this.editForm.get(['daysOfExposure']).value,
      formulation: this.editForm.get(['formulation']).value,
      study: this.editForm.get(['study']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStudyProduct>>) {
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

  trackClinicalStudyById(index: number, item: IClinicalStudy) {
    return item.id;
  }
}
