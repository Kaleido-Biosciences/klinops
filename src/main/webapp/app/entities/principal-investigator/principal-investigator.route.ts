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
import { PrincipalInvestigator } from 'app/shared/model/principal-investigator.model';
import { PrincipalInvestigatorService } from './principal-investigator.service';
import { PrincipalInvestigatorComponent } from './principal-investigator.component';
import { PrincipalInvestigatorDetailComponent } from './principal-investigator-detail.component';
import { PrincipalInvestigatorUpdateComponent } from './principal-investigator-update.component';
import { PrincipalInvestigatorDeletePopupComponent } from './principal-investigator-delete-dialog.component';
import { IPrincipalInvestigator } from 'app/shared/model/principal-investigator.model';

@Injectable({ providedIn: 'root' })
export class PrincipalInvestigatorResolve implements Resolve<IPrincipalInvestigator> {
  constructor(private service: PrincipalInvestigatorService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IPrincipalInvestigator> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<PrincipalInvestigator>) => response.ok),
        map((principalInvestigator: HttpResponse<PrincipalInvestigator>) => principalInvestigator.body)
      );
    }
    return of(new PrincipalInvestigator());
  }
}

export const principalInvestigatorRoute: Routes = [
  {
    path: '',
    component: PrincipalInvestigatorComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'klinopsApp.principalInvestigator.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PrincipalInvestigatorDetailComponent,
    resolve: {
      principalInvestigator: PrincipalInvestigatorResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'klinopsApp.principalInvestigator.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PrincipalInvestigatorUpdateComponent,
    resolve: {
      principalInvestigator: PrincipalInvestigatorResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'klinopsApp.principalInvestigator.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PrincipalInvestigatorUpdateComponent,
    resolve: {
      principalInvestigator: PrincipalInvestigatorResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'klinopsApp.principalInvestigator.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const principalInvestigatorPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: PrincipalInvestigatorDeletePopupComponent,
    resolve: {
      principalInvestigator: PrincipalInvestigatorResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'klinopsApp.principalInvestigator.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
