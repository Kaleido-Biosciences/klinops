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
import { IStudyEndPoint, StudyEndPoint } from 'app/shared/model/study-end-point.model';
import { StudyEndPointService } from './study-end-point.service';
import { IClinicalStudy } from 'app/shared/model/clinical-study.model';
import { ClinicalStudyService } from 'app/entities/clinical-study/clinical-study.service';

@Component({
  selector: 'ko-study-end-point-update',
  templateUrl: './study-end-point-update.component.html'
})
export class StudyEndPointUpdateComponent implements OnInit {
  isSaving: boolean;

  clinicalstudies: IClinicalStudy[];

  editForm = this.fb.group({
    id: [],
    description: [null, [Validators.required, Validators.maxLength(5000)]],
    objective: [null, [Validators.required, Validators.maxLength(5000)]],
    endPointType: [null, [Validators.required]],
    study: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected studyEndPointService: StudyEndPointService,
    protected clinicalStudyService: ClinicalStudyService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ studyEndPoint }) => {
      this.updateForm(studyEndPoint);
    });
    this.clinicalStudyService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IClinicalStudy[]>) => mayBeOk.ok),
        map((response: HttpResponse<IClinicalStudy[]>) => response.body)
      )
      .subscribe((res: IClinicalStudy[]) => (this.clinicalstudies = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(studyEndPoint: IStudyEndPoint) {
    this.editForm.patchValue({
      id: studyEndPoint.id,
      description: studyEndPoint.description,
      objective: studyEndPoint.objective,
      endPointType: studyEndPoint.endPointType,
      study: studyEndPoint.study
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const studyEndPoint = this.createFromForm();
    if (studyEndPoint.id !== undefined) {
      this.subscribeToSaveResponse(this.studyEndPointService.update(studyEndPoint));
    } else {
      this.subscribeToSaveResponse(this.studyEndPointService.create(studyEndPoint));
    }
  }

  private createFromForm(): IStudyEndPoint {
    return {
      ...new StudyEndPoint(),
      id: this.editForm.get(['id']).value,
      description: this.editForm.get(['description']).value,
      objective: this.editForm.get(['objective']).value,
      endPointType: this.editForm.get(['endPointType']).value,
      study: this.editForm.get(['study']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStudyEndPoint>>) {
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
