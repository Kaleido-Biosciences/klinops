import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IClinicalStudy, ClinicalStudy } from 'app/shared/model/clinical-study.model';
import { ClinicalStudyService } from './clinical-study.service';
import { ITrialMasterFile } from 'app/shared/model/trial-master-file.model';
import { TrialMasterFileService } from 'app/entities/trial-master-file/trial-master-file.service';
import { IPrincipalInvestigator } from 'app/shared/model/principal-investigator.model';
import { PrincipalInvestigatorService } from 'app/entities/principal-investigator/principal-investigator.service';

@Component({
  selector: 'ko-clinical-study-update',
  templateUrl: './clinical-study-update.component.html'
})
export class ClinicalStudyUpdateComponent implements OnInit {
  isSaving: boolean;

  masterfiles: ITrialMasterFile[];

  principalinvestigators: IPrincipalInvestigator[];

  editForm = this.fb.group({
    id: [],
    studyIdentifier: [null, []],
    phase: [],
    status: [],
    sequence: [],
    studyYear: [null, [Validators.min(1900)]],
    name: [null, [Validators.maxLength(500)]],
    design: [],
    numberOfCohorts: [null, [Validators.min(0)]],
    intendedSubjectsPerCohort: [null, [Validators.min(0)]],
    populationDiseaseState: [],
    minimumAge: [null, [Validators.min(0)]],
    maximumAge: [null, [Validators.min(0)]],
    subjectsEnrolled: [null, [Validators.min(0)]],
    femalesEligible: [],
    malesEligible: [],
    studyShortName: [null, [Validators.required, Validators.maxLength(128)]],
    projectManager: [null, [Validators.required]],
    principalPhysician: [],
    researchRepresentative: [],
    analysisRepresentative: [],
    dataManager: [],
    masterFile: [],
    investigators: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected clinicalStudyService: ClinicalStudyService,
    protected trialMasterFileService: TrialMasterFileService,
    protected principalInvestigatorService: PrincipalInvestigatorService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ clinicalStudy }) => {
      this.updateForm(clinicalStudy);
    });
    this.trialMasterFileService
      .query({ 'clinicalStudyId.specified': 'false' })
      .pipe(
        filter((mayBeOk: HttpResponse<ITrialMasterFile[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITrialMasterFile[]>) => response.body)
      )
      .subscribe(
        (res: ITrialMasterFile[]) => {
          if (!this.editForm.get('masterFile').value || !this.editForm.get('masterFile').value.id) {
            this.masterfiles = res;
          } else {
            this.trialMasterFileService
              .find(this.editForm.get('masterFile').value.id)
              .pipe(
                filter((subResMayBeOk: HttpResponse<ITrialMasterFile>) => subResMayBeOk.ok),
                map((subResponse: HttpResponse<ITrialMasterFile>) => subResponse.body)
              )
              .subscribe(
                (subRes: ITrialMasterFile) => (this.masterfiles = [subRes].concat(res)),
                (subRes: HttpErrorResponse) => this.onError(subRes.message)
              );
          }
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
    this.principalInvestigatorService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IPrincipalInvestigator[]>) => mayBeOk.ok),
        map((response: HttpResponse<IPrincipalInvestigator[]>) => response.body)
      )
      .subscribe(
        (res: IPrincipalInvestigator[]) => (this.principalinvestigators = res),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  updateForm(clinicalStudy: IClinicalStudy) {
    this.editForm.patchValue({
      id: clinicalStudy.id,
      studyIdentifier: clinicalStudy.studyIdentifier,
      phase: clinicalStudy.phase,
      status: clinicalStudy.status,
      sequence: clinicalStudy.sequence,
      studyYear: clinicalStudy.studyYear,
      name: clinicalStudy.name,
      design: clinicalStudy.design,
      numberOfCohorts: clinicalStudy.numberOfCohorts,
      intendedSubjectsPerCohort: clinicalStudy.intendedSubjectsPerCohort,
      populationDiseaseState: clinicalStudy.populationDiseaseState,
      minimumAge: clinicalStudy.minimumAge,
      maximumAge: clinicalStudy.maximumAge,
      subjectsEnrolled: clinicalStudy.subjectsEnrolled,
      femalesEligible: clinicalStudy.femalesEligible,
      malesEligible: clinicalStudy.malesEligible,
      studyShortName: clinicalStudy.studyShortName,
      projectManager: clinicalStudy.projectManager,
      principalPhysician: clinicalStudy.principalPhysician,
      researchRepresentative: clinicalStudy.researchRepresentative,
      analysisRepresentative: clinicalStudy.analysisRepresentative,
      dataManager: clinicalStudy.dataManager,
      masterFile: clinicalStudy.masterFile,
      investigators: clinicalStudy.investigators
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const clinicalStudy = this.createFromForm();
    if (clinicalStudy.id !== undefined) {
      this.subscribeToSaveResponse(this.clinicalStudyService.update(clinicalStudy));
    } else {
      this.subscribeToSaveResponse(this.clinicalStudyService.create(clinicalStudy));
    }
  }

  private createFromForm(): IClinicalStudy {
    return {
      ...new ClinicalStudy(),
      id: this.editForm.get(['id']).value,
      studyIdentifier: this.editForm.get(['studyIdentifier']).value,
      phase: this.editForm.get(['phase']).value,
      status: this.editForm.get(['status']).value,
      sequence: this.editForm.get(['sequence']).value,
      studyYear: this.editForm.get(['studyYear']).value,
      name: this.editForm.get(['name']).value,
      design: this.editForm.get(['design']).value,
      numberOfCohorts: this.editForm.get(['numberOfCohorts']).value,
      intendedSubjectsPerCohort: this.editForm.get(['intendedSubjectsPerCohort']).value,
      populationDiseaseState: this.editForm.get(['populationDiseaseState']).value,
      minimumAge: this.editForm.get(['minimumAge']).value,
      maximumAge: this.editForm.get(['maximumAge']).value,
      subjectsEnrolled: this.editForm.get(['subjectsEnrolled']).value,
      femalesEligible: this.editForm.get(['femalesEligible']).value,
      malesEligible: this.editForm.get(['malesEligible']).value,
      studyShortName: this.editForm.get(['studyShortName']).value,
      projectManager: this.editForm.get(['projectManager']).value,
      principalPhysician: this.editForm.get(['principalPhysician']).value,
      researchRepresentative: this.editForm.get(['researchRepresentative']).value,
      analysisRepresentative: this.editForm.get(['analysisRepresentative']).value,
      dataManager: this.editForm.get(['dataManager']).value,
      masterFile: this.editForm.get(['masterFile']).value,
      investigators: this.editForm.get(['investigators']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClinicalStudy>>) {
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

  trackTrialMasterFileById(index: number, item: ITrialMasterFile) {
    return item.id;
  }

  trackPrincipalInvestigatorById(index: number, item: IPrincipalInvestigator) {
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
