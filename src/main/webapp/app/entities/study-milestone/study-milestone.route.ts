import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StudyMilestone } from 'app/shared/model/study-milestone.model';
import { StudyMilestoneService } from './study-milestone.service';
import { StudyMilestoneComponent } from './study-milestone.component';
import { StudyMilestoneDetailComponent } from './study-milestone-detail.component';
import { StudyMilestoneUpdateComponent } from './study-milestone-update.component';
import { StudyMilestoneDeletePopupComponent } from './study-milestone-delete-dialog.component';
import { IStudyMilestone } from 'app/shared/model/study-milestone.model';

@Injectable({ providedIn: 'root' })
export class StudyMilestoneResolve implements Resolve<IStudyMilestone> {
  constructor(private service: StudyMilestoneService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IStudyMilestone> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<StudyMilestone>) => response.ok),
        map((studyMilestone: HttpResponse<StudyMilestone>) => studyMilestone.body)
      );
    }
    return of(new StudyMilestone());
  }
}

export const studyMilestoneRoute: Routes = [
  {
    path: '',
    component: StudyMilestoneComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'klinopsApp.studyMilestone.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: StudyMilestoneDetailComponent,
    resolve: {
      studyMilestone: StudyMilestoneResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'klinopsApp.studyMilestone.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: StudyMilestoneUpdateComponent,
    resolve: {
      studyMilestone: StudyMilestoneResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'klinopsApp.studyMilestone.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: StudyMilestoneUpdateComponent,
    resolve: {
      studyMilestone: StudyMilestoneResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'klinopsApp.studyMilestone.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const studyMilestonePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: StudyMilestoneDeletePopupComponent,
    resolve: {
      studyMilestone: StudyMilestoneResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'klinopsApp.studyMilestone.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
