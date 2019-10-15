/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { TrialMasterFile } from 'app/shared/model/trial-master-file.model';
import { TrialMasterFileService } from './trial-master-file.service';
import { TrialMasterFileComponent } from './trial-master-file.component';
import { TrialMasterFileDetailComponent } from './trial-master-file-detail.component';
import { TrialMasterFileUpdateComponent } from './trial-master-file-update.component';
import { TrialMasterFileDeletePopupComponent } from './trial-master-file-delete-dialog.component';
import { ITrialMasterFile } from 'app/shared/model/trial-master-file.model';

@Injectable({ providedIn: 'root' })
export class TrialMasterFileResolve implements Resolve<ITrialMasterFile> {
  constructor(private service: TrialMasterFileService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ITrialMasterFile> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<TrialMasterFile>) => response.ok),
        map((trialMasterFile: HttpResponse<TrialMasterFile>) => trialMasterFile.body)
      );
    }
    return of(new TrialMasterFile());
  }
}

export const trialMasterFileRoute: Routes = [
  {
    path: '',
    component: TrialMasterFileComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'klinopsApp.trialMasterFile.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: TrialMasterFileDetailComponent,
    resolve: {
      trialMasterFile: TrialMasterFileResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'klinopsApp.trialMasterFile.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: TrialMasterFileUpdateComponent,
    resolve: {
      trialMasterFile: TrialMasterFileResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'klinopsApp.trialMasterFile.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: TrialMasterFileUpdateComponent,
    resolve: {
      trialMasterFile: TrialMasterFileResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'klinopsApp.trialMasterFile.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const trialMasterFilePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: TrialMasterFileDeletePopupComponent,
    resolve: {
      trialMasterFile: TrialMasterFileResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'klinopsApp.trialMasterFile.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
