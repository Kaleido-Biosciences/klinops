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
import { IStudyMilestone, StudyMilestone } from 'app/shared/model/study-milestone.model';
import { StudyMilestoneService } from './study-milestone.service';
import { IClinicalStudy } from 'app/shared/model/clinical-study.model';
import { ClinicalStudyService } from 'app/entities/clinical-study/clinical-study.service';

@Component({
  selector: 'ko-study-milestone-update',
  templateUrl: './study-milestone-update.component.html'
})
export class StudyMilestoneUpdateComponent implements OnInit {
  isSaving: boolean;

  clinicalstudies: IClinicalStudy[];
  projectedCompletionDateDp: any;
  actualCompletionDateDp: any;

  editForm = this.fb.group({
    id: [],
    mileStoneName: [null, [Validators.required]],
    mileStoneType: [null, [Validators.required]],
    projectedCompletionDate: [],
    actualCompletionDate: [],
    study: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected studyMilestoneService: StudyMilestoneService,
    protected clinicalStudyService: ClinicalStudyService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ studyMilestone }) => {
      this.updateForm(studyMilestone);
    });
    this.clinicalStudyService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IClinicalStudy[]>) => mayBeOk.ok),
        map((response: HttpResponse<IClinicalStudy[]>) => response.body)
      )
      .subscribe((res: IClinicalStudy[]) => (this.clinicalstudies = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(studyMilestone: IStudyMilestone) {
    this.editForm.patchValue({
      id: studyMilestone.id,
      mileStoneName: studyMilestone.mileStoneName,
      mileStoneType: studyMilestone.mileStoneType,
      projectedCompletionDate: studyMilestone.projectedCompletionDate,
      actualCompletionDate: studyMilestone.actualCompletionDate,
      study: studyMilestone.study
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const studyMilestone = this.createFromForm();
    if (studyMilestone.id !== undefined) {
      this.subscribeToSaveResponse(this.studyMilestoneService.update(studyMilestone));
    } else {
      this.subscribeToSaveResponse(this.studyMilestoneService.create(studyMilestone));
    }
  }

  private createFromForm(): IStudyMilestone {
    return {
      ...new StudyMilestone(),
      id: this.editForm.get(['id']).value,
      mileStoneName: this.editForm.get(['mileStoneName']).value,
      mileStoneType: this.editForm.get(['mileStoneType']).value,
      projectedCompletionDate: this.editForm.get(['projectedCompletionDate']).value,
      actualCompletionDate: this.editForm.get(['actualCompletionDate']).value,
      study: this.editForm.get(['study']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStudyMilestone>>) {
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
