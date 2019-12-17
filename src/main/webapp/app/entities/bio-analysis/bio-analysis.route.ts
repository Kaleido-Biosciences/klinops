import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { BioAnalysis } from 'app/shared/model/bio-analysis.model';
import { BioAnalysisService } from './bio-analysis.service';
import { BioAnalysisComponent } from './bio-analysis.component';
import { BioAnalysisDetailComponent } from './bio-analysis-detail.component';
import { BioAnalysisUpdateComponent } from './bio-analysis-update.component';
import { BioAnalysisDeletePopupComponent } from './bio-analysis-delete-dialog.component';
import { IBioAnalysis } from 'app/shared/model/bio-analysis.model';

@Injectable({ providedIn: 'root' })
export class BioAnalysisResolve implements Resolve<IBioAnalysis> {
  constructor(private service: BioAnalysisService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IBioAnalysis> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<BioAnalysis>) => response.ok),
        map((bioAnalysis: HttpResponse<BioAnalysis>) => bioAnalysis.body)
      );
    }
    return of(new BioAnalysis());
  }
}

export const bioAnalysisRoute: Routes = [
  {
    path: '',
    component: BioAnalysisComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'klinopsApp.bioAnalysis.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: BioAnalysisDetailComponent,
    resolve: {
      bioAnalysis: BioAnalysisResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'klinopsApp.bioAnalysis.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: BioAnalysisUpdateComponent,
    resolve: {
      bioAnalysis: BioAnalysisResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'klinopsApp.bioAnalysis.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: BioAnalysisUpdateComponent,
    resolve: {
      bioAnalysis: BioAnalysisResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'klinopsApp.bioAnalysis.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const bioAnalysisPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: BioAnalysisDeletePopupComponent,
    resolve: {
      bioAnalysis: BioAnalysisResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'klinopsApp.bioAnalysis.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
