import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ITrialMasterFile, TrialMasterFile } from 'app/shared/model/trial-master-file.model';
import { TrialMasterFileService } from './trial-master-file.service';
import { IClinicalStudy } from 'app/shared/model/clinical-study.model';
import { ClinicalStudyService } from 'app/entities/clinical-study/clinical-study.service';

@Component({
  selector: 'ko-trial-master-file-update',
  templateUrl: './trial-master-file-update.component.html'
})
export class TrialMasterFileUpdateComponent implements OnInit {
  isSaving: boolean;

  clinicalstudies: IClinicalStudy[];

  editForm = this.fb.group({
    id: [],
    fileName: [null, []],
    location: [null, [Validators.required]],
    status: [],
    electronic: [null, [Validators.required]]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected trialMasterFileService: TrialMasterFileService,
    protected clinicalStudyService: ClinicalStudyService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ trialMasterFile }) => {
      this.updateForm(trialMasterFile);
    });
    this.clinicalStudyService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IClinicalStudy[]>) => mayBeOk.ok),
        map((response: HttpResponse<IClinicalStudy[]>) => response.body)
      )
      .subscribe((res: IClinicalStudy[]) => (this.clinicalstudies = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(trialMasterFile: ITrialMasterFile) {
    this.editForm.patchValue({
      id: trialMasterFile.id,
      fileName: trialMasterFile.fileName,
      location: trialMasterFile.location,
      status: trialMasterFile.status,
      electronic: trialMasterFile.electronic
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const trialMasterFile = this.createFromForm();
    if (trialMasterFile.id !== undefined) {
      this.subscribeToSaveResponse(this.trialMasterFileService.update(trialMasterFile));
    } else {
      this.subscribeToSaveResponse(this.trialMasterFileService.create(trialMasterFile));
    }
  }

  private createFromForm(): ITrialMasterFile {
    return {
      ...new TrialMasterFile(),
      id: this.editForm.get(['id']).value,
      fileName: this.editForm.get(['fileName']).value,
      location: this.editForm.get(['location']).value,
      status: this.editForm.get(['status']).value,
      electronic: this.editForm.get(['electronic']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrialMasterFile>>) {
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
