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
import { StudySample } from 'app/shared/model/study-sample.model';
import { StudySampleService } from './study-sample.service';
import { StudySampleComponent } from './study-sample.component';
import { StudySampleDetailComponent } from './study-sample-detail.component';
import { StudySampleUpdateComponent } from './study-sample-update.component';
import { StudySampleDeletePopupComponent } from './study-sample-delete-dialog.component';
import { IStudySample } from 'app/shared/model/study-sample.model';

@Injectable({ providedIn: 'root' })
export class StudySampleResolve implements Resolve<IStudySample> {
  constructor(private service: StudySampleService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IStudySample> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<StudySample>) => response.ok),
        map((studySample: HttpResponse<StudySample>) => studySample.body)
      );
    }
    return of(new StudySample());
  }
}

export const studySampleRoute: Routes = [
  {
    path: '',
    component: StudySampleComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'klinopsApp.studySample.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: StudySampleDetailComponent,
    resolve: {
      studySample: StudySampleResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'klinopsApp.studySample.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: StudySampleUpdateComponent,
    resolve: {
      studySample: StudySampleResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'klinopsApp.studySample.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: StudySampleUpdateComponent,
    resolve: {
      studySample: StudySampleResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'klinopsApp.studySample.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const studySamplePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: StudySampleDeletePopupComponent,
    resolve: {
      studySample: StudySampleResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'klinopsApp.studySample.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
