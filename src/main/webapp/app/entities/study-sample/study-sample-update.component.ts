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
import { IStudySample, StudySample } from 'app/shared/model/study-sample.model';
import { StudySampleService } from './study-sample.service';
import { IClinicalStudy } from 'app/shared/model/clinical-study.model';
import { ClinicalStudyService } from 'app/entities/clinical-study/clinical-study.service';

@Component({
  selector: 'ko-study-sample-update',
  templateUrl: './study-sample-update.component.html'
})
export class StudySampleUpdateComponent implements OnInit {
  isSaving: boolean;

  clinicalstudies: IClinicalStudy[];

  editForm = this.fb.group({
    id: [],
    sampleType: [null, [Validators.required]],
    expectedNumberOfSamples: [null, [Validators.min(0)]],
    study: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected studySampleService: StudySampleService,
    protected clinicalStudyService: ClinicalStudyService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ studySample }) => {
      this.updateForm(studySample);
    });
    this.clinicalStudyService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IClinicalStudy[]>) => mayBeOk.ok),
        map((response: HttpResponse<IClinicalStudy[]>) => response.body)
      )
      .subscribe((res: IClinicalStudy[]) => (this.clinicalstudies = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(studySample: IStudySample) {
    this.editForm.patchValue({
      id: studySample.id,
      sampleType: studySample.sampleType,
      expectedNumberOfSamples: studySample.expectedNumberOfSamples,
      study: studySample.study
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const studySample = this.createFromForm();
    if (studySample.id !== undefined) {
      this.subscribeToSaveResponse(this.studySampleService.update(studySample));
    } else {
      this.subscribeToSaveResponse(this.studySampleService.create(studySample));
    }
  }

  private createFromForm(): IStudySample {
    return {
      ...new StudySample(),
      id: this.editForm.get(['id']).value,
      sampleType: this.editForm.get(['sampleType']).value,
      expectedNumberOfSamples: this.editForm.get(['expectedNumberOfSamples']).value,
      study: this.editForm.get(['study']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStudySample>>) {
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
