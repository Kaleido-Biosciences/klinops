import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StudyEndPoint } from 'app/shared/model/study-end-point.model';
import { StudyEndPointService } from './study-end-point.service';
import { StudyEndPointComponent } from './study-end-point.component';
import { StudyEndPointDetailComponent } from './study-end-point-detail.component';
import { StudyEndPointUpdateComponent } from './study-end-point-update.component';
import { StudyEndPointDeletePopupComponent } from './study-end-point-delete-dialog.component';
import { IStudyEndPoint } from 'app/shared/model/study-end-point.model';

@Injectable({ providedIn: 'root' })
export class StudyEndPointResolve implements Resolve<IStudyEndPoint> {
  constructor(private service: StudyEndPointService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IStudyEndPoint> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<StudyEndPoint>) => response.ok),
        map((studyEndPoint: HttpResponse<StudyEndPoint>) => studyEndPoint.body)
      );
    }
    return of(new StudyEndPoint());
  }
}

export const studyEndPointRoute: Routes = [
  {
    path: '',
    component: StudyEndPointComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'klinopsApp.studyEndPoint.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: StudyEndPointDetailComponent,
    resolve: {
      studyEndPoint: StudyEndPointResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'klinopsApp.studyEndPoint.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: StudyEndPointUpdateComponent,
    resolve: {
      studyEndPoint: StudyEndPointResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'klinopsApp.studyEndPoint.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: StudyEndPointUpdateComponent,
    resolve: {
      studyEndPoint: StudyEndPointResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'klinopsApp.studyEndPoint.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const studyEndPointPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: StudyEndPointDeletePopupComponent,
    resolve: {
      studyEndPoint: StudyEndPointResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'klinopsApp.studyEndPoint.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
