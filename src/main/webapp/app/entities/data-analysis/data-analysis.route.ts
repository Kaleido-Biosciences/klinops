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
import { DataAnalysis } from 'app/shared/model/data-analysis.model';
import { DataAnalysisService } from './data-analysis.service';
import { DataAnalysisComponent } from './data-analysis.component';
import { DataAnalysisDetailComponent } from './data-analysis-detail.component';
import { DataAnalysisUpdateComponent } from './data-analysis-update.component';
import { DataAnalysisDeletePopupComponent } from './data-analysis-delete-dialog.component';
import { IDataAnalysis } from 'app/shared/model/data-analysis.model';

@Injectable({ providedIn: 'root' })
export class DataAnalysisResolve implements Resolve<IDataAnalysis> {
  constructor(private service: DataAnalysisService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IDataAnalysis> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<DataAnalysis>) => response.ok),
        map((dataAnalysis: HttpResponse<DataAnalysis>) => dataAnalysis.body)
      );
    }
    return of(new DataAnalysis());
  }
}

export const dataAnalysisRoute: Routes = [
  {
    path: '',
    component: DataAnalysisComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'klinopsApp.dataAnalysis.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: DataAnalysisDetailComponent,
    resolve: {
      dataAnalysis: DataAnalysisResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'klinopsApp.dataAnalysis.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: DataAnalysisUpdateComponent,
    resolve: {
      dataAnalysis: DataAnalysisResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'klinopsApp.dataAnalysis.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: DataAnalysisUpdateComponent,
    resolve: {
      dataAnalysis: DataAnalysisResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'klinopsApp.dataAnalysis.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const dataAnalysisPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: DataAnalysisDeletePopupComponent,
    resolve: {
      dataAnalysis: DataAnalysisResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'klinopsApp.dataAnalysis.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
