import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IPrincipalInvestigator, PrincipalInvestigator } from 'app/shared/model/principal-investigator.model';
import { PrincipalInvestigatorService } from './principal-investigator.service';
import { ISite } from 'app/shared/model/site.model';
import { SiteService } from 'app/entities/site/site.service';
import { IClinicalStudy } from 'app/shared/model/clinical-study.model';
import { ClinicalStudyService } from 'app/entities/clinical-study/clinical-study.service';

@Component({
  selector: 'ko-principal-investigator-update',
  templateUrl: './principal-investigator-update.component.html'
})
export class PrincipalInvestigatorUpdateComponent implements OnInit {
  isSaving: boolean;

  sites: ISite[];

  clinicalstudies: IClinicalStudy[];

  editForm = this.fb.group({
    id: [],
    investigatorName: [null, [Validators.required]],
    streetAddress: [],
    city: [],
    state: [],
    zip: [],
    country: [],
    email: [null, [Validators.pattern('^(.+)@(.+)|$')]],
    phoneNumber: [],
    site: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected principalInvestigatorService: PrincipalInvestigatorService,
    protected siteService: SiteService,
    protected clinicalStudyService: ClinicalStudyService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ principalInvestigator }) => {
      this.updateForm(principalInvestigator);
    });
    this.siteService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ISite[]>) => mayBeOk.ok),
        map((response: HttpResponse<ISite[]>) => response.body)
      )
      .subscribe((res: ISite[]) => (this.sites = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.clinicalStudyService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IClinicalStudy[]>) => mayBeOk.ok),
        map((response: HttpResponse<IClinicalStudy[]>) => response.body)
      )
      .subscribe((res: IClinicalStudy[]) => (this.clinicalstudies = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(principalInvestigator: IPrincipalInvestigator) {
    this.editForm.patchValue({
      id: principalInvestigator.id,
      investigatorName: principalInvestigator.investigatorName,
      streetAddress: principalInvestigator.streetAddress,
      city: principalInvestigator.city,
      state: principalInvestigator.state,
      zip: principalInvestigator.zip,
      country: principalInvestigator.country,
      email: principalInvestigator.email,
      phoneNumber: principalInvestigator.phoneNumber,
      site: principalInvestigator.site
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const principalInvestigator = this.createFromForm();
    if (principalInvestigator.id !== undefined) {
      this.subscribeToSaveResponse(this.principalInvestigatorService.update(principalInvestigator));
    } else {
      this.subscribeToSaveResponse(this.principalInvestigatorService.create(principalInvestigator));
    }
  }

  private createFromForm(): IPrincipalInvestigator {
    return {
      ...new PrincipalInvestigator(),
      id: this.editForm.get(['id']).value,
      investigatorName: this.editForm.get(['investigatorName']).value,
      streetAddress: this.editForm.get(['streetAddress']).value,
      city: this.editForm.get(['city']).value,
      state: this.editForm.get(['state']).value,
      zip: this.editForm.get(['zip']).value,
      country: this.editForm.get(['country']).value,
      email: this.editForm.get(['email']).value,
      phoneNumber: this.editForm.get(['phoneNumber']).value,
      site: this.editForm.get(['site']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPrincipalInvestigator>>) {
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

  trackSiteById(index: number, item: ISite) {
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
