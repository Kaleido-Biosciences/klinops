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
import { IDataAnalysis, DataAnalysis } from 'app/shared/model/data-analysis.model';
import { DataAnalysisService } from './data-analysis.service';
import { IBioAnalysis } from 'app/shared/model/bio-analysis.model';
import { BioAnalysisService } from 'app/entities/bio-analysis/bio-analysis.service';
import { IClinicalStudy } from 'app/shared/model/clinical-study.model';
import { ClinicalStudyService } from 'app/entities/clinical-study/clinical-study.service';

@Component({
  selector: 'ko-data-analysis-update',
  templateUrl: './data-analysis-update.component.html'
})
export class DataAnalysisUpdateComponent implements OnInit {
  isSaving: boolean;

  bioanalyses: IBioAnalysis[];

  clinicalstudies: IClinicalStudy[];
  anticipatedAnalysisDeliveryDateDp: any;
  actualAnalysisDeliveryDateDp: any;

  editForm = this.fb.group({
    id: [],
    dataAnalysesType: [null, [Validators.required]],
    contactName: [],
    contactEmail: [null, [Validators.pattern('^(.+)@(.+)|$')]],
    anticipatedAnalysisDeliveryDate: [],
    actualAnalysisDeliveryDate: [],
    dataLocation: [],
    bioAnalyses: [],
    study: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected dataAnalysisService: DataAnalysisService,
    protected bioAnalysisService: BioAnalysisService,
    protected clinicalStudyService: ClinicalStudyService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ dataAnalysis }) => {
      this.updateForm(dataAnalysis);
    });
    this.bioAnalysisService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IBioAnalysis[]>) => mayBeOk.ok),
        map((response: HttpResponse<IBioAnalysis[]>) => response.body)
      )
      .subscribe((res: IBioAnalysis[]) => (this.bioanalyses = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.clinicalStudyService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IClinicalStudy[]>) => mayBeOk.ok),
        map((response: HttpResponse<IClinicalStudy[]>) => response.body)
      )
      .subscribe((res: IClinicalStudy[]) => (this.clinicalstudies = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(dataAnalysis: IDataAnalysis) {
    this.editForm.patchValue({
      id: dataAnalysis.id,
      dataAnalysesType: dataAnalysis.dataAnalysesType,
      contactName: dataAnalysis.contactName,
      contactEmail: dataAnalysis.contactEmail,
      anticipatedAnalysisDeliveryDate: dataAnalysis.anticipatedAnalysisDeliveryDate,
      actualAnalysisDeliveryDate: dataAnalysis.actualAnalysisDeliveryDate,
      dataLocation: dataAnalysis.dataLocation,
      bioAnalyses: dataAnalysis.bioAnalyses,
      study: dataAnalysis.study
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const dataAnalysis = this.createFromForm();
    if (dataAnalysis.id !== undefined) {
      this.subscribeToSaveResponse(this.dataAnalysisService.update(dataAnalysis));
    } else {
      this.subscribeToSaveResponse(this.dataAnalysisService.create(dataAnalysis));
    }
  }

  private createFromForm(): IDataAnalysis {
    return {
      ...new DataAnalysis(),
      id: this.editForm.get(['id']).value,
      dataAnalysesType: this.editForm.get(['dataAnalysesType']).value,
      contactName: this.editForm.get(['contactName']).value,
      contactEmail: this.editForm.get(['contactEmail']).value,
      anticipatedAnalysisDeliveryDate: this.editForm.get(['anticipatedAnalysisDeliveryDate']).value,
      actualAnalysisDeliveryDate: this.editForm.get(['actualAnalysisDeliveryDate']).value,
      dataLocation: this.editForm.get(['dataLocation']).value,
      bioAnalyses: this.editForm.get(['bioAnalyses']).value,
      study: this.editForm.get(['study']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDataAnalysis>>) {
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

  trackBioAnalysisById(index: number, item: IBioAnalysis) {
    return item.id;
  }

  trackClinicalStudyById(index: number, item: IClinicalStudy) {
    return item.id;
  }

  getSelected(selectedVals: any[], option: any) {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
