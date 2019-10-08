import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ClinicalStudy } from 'app/shared/model/clinical-study.model';
import { ClinicalStudyService } from './clinical-study.service';
import { ClinicalStudyComponent } from './clinical-study.component';
import { ClinicalStudyDetailComponent } from './clinical-study-detail.component';
import { ClinicalStudyUpdateComponent } from './clinical-study-update.component';
import { ClinicalStudyDeletePopupComponent } from './clinical-study-delete-dialog.component';
import { IClinicalStudy } from 'app/shared/model/clinical-study.model';

@Injectable({ providedIn: 'root' })
export class ClinicalStudyResolve implements Resolve<IClinicalStudy> {
  constructor(private service: ClinicalStudyService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IClinicalStudy> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<ClinicalStudy>) => response.ok),
        map((clinicalStudy: HttpResponse<ClinicalStudy>) => clinicalStudy.body)
      );
    }
    return of(new ClinicalStudy());
  }
}

export const clinicalStudyRoute: Routes = [
  {
    path: '',
    component: ClinicalStudyComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'klinopsApp.clinicalStudy.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ClinicalStudyDetailComponent,
    resolve: {
      clinicalStudy: ClinicalStudyResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'klinopsApp.clinicalStudy.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ClinicalStudyUpdateComponent,
    resolve: {
      clinicalStudy: ClinicalStudyResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'klinopsApp.clinicalStudy.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ClinicalStudyUpdateComponent,
    resolve: {
      clinicalStudy: ClinicalStudyResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'klinopsApp.clinicalStudy.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const clinicalStudyPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ClinicalStudyDeletePopupComponent,
    resolve: {
      clinicalStudy: ClinicalStudyResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'klinopsApp.clinicalStudy.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
