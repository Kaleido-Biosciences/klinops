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
import { StudyProduct } from 'app/shared/model/study-product.model';
import { StudyProductService } from './study-product.service';
import { StudyProductComponent } from './study-product.component';
import { StudyProductDetailComponent } from './study-product-detail.component';
import { StudyProductUpdateComponent } from './study-product-update.component';
import { StudyProductDeletePopupComponent } from './study-product-delete-dialog.component';
import { IStudyProduct } from 'app/shared/model/study-product.model';

@Injectable({ providedIn: 'root' })
export class StudyProductResolve implements Resolve<IStudyProduct> {
  constructor(private service: StudyProductService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IStudyProduct> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<StudyProduct>) => response.ok),
        map((studyProduct: HttpResponse<StudyProduct>) => studyProduct.body)
      );
    }
    return of(new StudyProduct());
  }
}

export const studyProductRoute: Routes = [
  {
    path: '',
    component: StudyProductComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'klinopsApp.studyProduct.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: StudyProductDetailComponent,
    resolve: {
      studyProduct: StudyProductResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'klinopsApp.studyProduct.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: StudyProductUpdateComponent,
    resolve: {
      studyProduct: StudyProductResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'klinopsApp.studyProduct.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: StudyProductUpdateComponent,
    resolve: {
      studyProduct: StudyProductResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'klinopsApp.studyProduct.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const studyProductPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: StudyProductDeletePopupComponent,
    resolve: {
      studyProduct: StudyProductResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'klinopsApp.studyProduct.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
