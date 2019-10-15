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
import { ShipmentComponent } from 'app/shared/model/shipment-component.model';
import { ShipmentComponentService } from './shipment-component.service';
import { ShipmentComponentComponent } from './shipment-component.component';
import { ShipmentComponentDetailComponent } from './shipment-component-detail.component';
import { ShipmentComponentUpdateComponent } from './shipment-component-update.component';
import { ShipmentComponentDeletePopupComponent } from './shipment-component-delete-dialog.component';
import { IShipmentComponent } from 'app/shared/model/shipment-component.model';

@Injectable({ providedIn: 'root' })
export class ShipmentComponentResolve implements Resolve<IShipmentComponent> {
  constructor(private service: ShipmentComponentService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IShipmentComponent> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<ShipmentComponent>) => response.ok),
        map((shipmentComponent: HttpResponse<ShipmentComponent>) => shipmentComponent.body)
      );
    }
    return of(new ShipmentComponent());
  }
}

export const shipmentComponentRoute: Routes = [
  {
    path: '',
    component: ShipmentComponentComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'klinopsApp.shipmentComponent.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ShipmentComponentDetailComponent,
    resolve: {
      shipmentComponent: ShipmentComponentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'klinopsApp.shipmentComponent.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ShipmentComponentUpdateComponent,
    resolve: {
      shipmentComponent: ShipmentComponentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'klinopsApp.shipmentComponent.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ShipmentComponentUpdateComponent,
    resolve: {
      shipmentComponent: ShipmentComponentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'klinopsApp.shipmentComponent.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const shipmentComponentPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ShipmentComponentDeletePopupComponent,
    resolve: {
      shipmentComponent: ShipmentComponentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'klinopsApp.shipmentComponent.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
