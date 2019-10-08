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
import { IBioAnalysis, BioAnalysis } from 'app/shared/model/bio-analysis.model';
import { BioAnalysisService } from './bio-analysis.service';
import { IStudyEndPoint } from 'app/shared/model/study-end-point.model';
import { StudyEndPointService } from 'app/entities/study-end-point/study-end-point.service';
import { ILaboratory } from 'app/shared/model/laboratory.model';
import { LaboratoryService } from 'app/entities/laboratory/laboratory.service';
import { IClinicalStudy } from 'app/shared/model/clinical-study.model';
import { ClinicalStudyService } from 'app/entities/clinical-study/clinical-study.service';
import { IDataAnalysis } from 'app/shared/model/data-analysis.model';
import { DataAnalysisService } from 'app/entities/data-analysis/data-analysis.service';

@Component({
  selector: 'ko-bio-analysis-update',
  templateUrl: './bio-analysis-update.component.html'
})
export class BioAnalysisUpdateComponent implements OnInit {
  isSaving: boolean;

  studyendpoints: IStudyEndPoint[];

  laboratories: ILaboratory[];

  clinicalstudies: IClinicalStudy[];

  dataanalyses: IDataAnalysis[];
  anticipatedLabWorkStartDateDp: any;
  actualLabWorkStartDateDp: any;
  anticipatedLabResultDeliveryDateDp: any;
  actualLabResultDeliveryDateDp: any;

  editForm = this.fb.group({
    id: [],
    analyte: [null, [Validators.required]],
    sampleType: [null, [Validators.required]],
    bioAnalysisType: [null, [Validators.required]],
    anticipatedLabWorkStartDate: [],
    actualLabWorkStartDate: [],
    anticipatedLabResultDeliveryDate: [],
    actualLabResultDeliveryDate: [],
    dataLocation: [],
    contactName: [],
    contactEmail: [null, [Validators.pattern('^(.+)@(.+)|$')]],
    comments: [],
    studyEndPoint: [],
    laboratories: [],
    study: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected bioAnalysisService: BioAnalysisService,
    protected studyEndPointService: StudyEndPointService,
    protected laboratoryService: LaboratoryService,
    protected clinicalStudyService: ClinicalStudyService,
    protected dataAnalysisService: DataAnalysisService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ bioAnalysis }) => {
      this.updateForm(bioAnalysis);
    });
    this.studyEndPointService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IStudyEndPoint[]>) => mayBeOk.ok),
        map((response: HttpResponse<IStudyEndPoint[]>) => response.body)
      )
      .subscribe((res: IStudyEndPoint[]) => (this.studyendpoints = res), (res: HttpErrorResponse) => this.onError(res.message));
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
    this.dataAnalysisService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IDataAnalysis[]>) => mayBeOk.ok),
        map((response: HttpResponse<IDataAnalysis[]>) => response.body)
      )
      .subscribe((res: IDataAnalysis[]) => (this.dataanalyses = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(bioAnalysis: IBioAnalysis) {
    this.editForm.patchValue({
      id: bioAnalysis.id,
      analyte: bioAnalysis.analyte,
      sampleType: bioAnalysis.sampleType,
      bioAnalysisType: bioAnalysis.bioAnalysisType,
      anticipatedLabWorkStartDate: bioAnalysis.anticipatedLabWorkStartDate,
      actualLabWorkStartDate: bioAnalysis.actualLabWorkStartDate,
      anticipatedLabResultDeliveryDate: bioAnalysis.anticipatedLabResultDeliveryDate,
      actualLabResultDeliveryDate: bioAnalysis.actualLabResultDeliveryDate,
      dataLocation: bioAnalysis.dataLocation,
      contactName: bioAnalysis.contactName,
      contactEmail: bioAnalysis.contactEmail,
      comments: bioAnalysis.comments,
      studyEndPoint: bioAnalysis.studyEndPoint,
      laboratories: bioAnalysis.laboratories,
      study: bioAnalysis.study
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const bioAnalysis = this.createFromForm();
    if (bioAnalysis.id !== undefined) {
      this.subscribeToSaveResponse(this.bioAnalysisService.update(bioAnalysis));
    } else {
      this.subscribeToSaveResponse(this.bioAnalysisService.create(bioAnalysis));
    }
  }

  private createFromForm(): IBioAnalysis {
    return {
      ...new BioAnalysis(),
      id: this.editForm.get(['id']).value,
      analyte: this.editForm.get(['analyte']).value,
      sampleType: this.editForm.get(['sampleType']).value,
      bioAnalysisType: this.editForm.get(['bioAnalysisType']).value,
      anticipatedLabWorkStartDate: this.editForm.get(['anticipatedLabWorkStartDate']).value,
      actualLabWorkStartDate: this.editForm.get(['actualLabWorkStartDate']).value,
      anticipatedLabResultDeliveryDate: this.editForm.get(['anticipatedLabResultDeliveryDate']).value,
      actualLabResultDeliveryDate: this.editForm.get(['actualLabResultDeliveryDate']).value,
      dataLocation: this.editForm.get(['dataLocation']).value,
      contactName: this.editForm.get(['contactName']).value,
      contactEmail: this.editForm.get(['contactEmail']).value,
      comments: this.editForm.get(['comments']).value,
      studyEndPoint: this.editForm.get(['studyEndPoint']).value,
      laboratories: this.editForm.get(['laboratories']).value,
      study: this.editForm.get(['study']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBioAnalysis>>) {
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

  trackStudyEndPointById(index: number, item: IStudyEndPoint) {
    return item.id;
  }

  trackLaboratoryById(index: number, item: ILaboratory) {
    return item.id;
  }

  trackClinicalStudyById(index: number, item: IClinicalStudy) {
    return item.id;
  }

  trackDataAnalysisById(index: number, item: IDataAnalysis) {
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
