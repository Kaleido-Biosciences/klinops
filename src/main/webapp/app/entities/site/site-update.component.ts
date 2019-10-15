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
import { ISite, Site } from 'app/shared/model/site.model';
import { SiteService } from './site.service';

@Component({
  selector: 'ko-site-update',
  templateUrl: './site-update.component.html'
})
export class SiteUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    siteName: [null, [Validators.required]],
    institution: [null, [Validators.required]],
    streetAddress: [null, [Validators.required]],
    city: [null, [Validators.required]],
    state: [],
    zip: [null, [Validators.required]],
    country: [null, [Validators.required]]
  });

  constructor(protected siteService: SiteService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ site }) => {
      this.updateForm(site);
    });
  }

  updateForm(site: ISite) {
    this.editForm.patchValue({
      id: site.id,
      siteName: site.siteName,
      institution: site.institution,
      streetAddress: site.streetAddress,
      city: site.city,
      state: site.state,
      zip: site.zip,
      country: site.country
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const site = this.createFromForm();
    if (site.id !== undefined) {
      this.subscribeToSaveResponse(this.siteService.update(site));
    } else {
      this.subscribeToSaveResponse(this.siteService.create(site));
    }
  }

  private createFromForm(): ISite {
    return {
      ...new Site(),
      id: this.editForm.get(['id']).value,
      siteName: this.editForm.get(['siteName']).value,
      institution: this.editForm.get(['institution']).value,
      streetAddress: this.editForm.get(['streetAddress']).value,
      city: this.editForm.get(['city']).value,
      state: this.editForm.get(['state']).value,
      zip: this.editForm.get(['zip']).value,
      country: this.editForm.get(['country']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISite>>) {
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
