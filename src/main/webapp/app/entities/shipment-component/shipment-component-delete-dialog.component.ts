/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IShipmentComponent } from 'app/shared/model/shipment-component.model';
import { ShipmentComponentService } from './shipment-component.service';

@Component({
  selector: 'ko-shipment-component-delete-dialog',
  templateUrl: './shipment-component-delete-dialog.component.html'
})
export class ShipmentComponentDeleteDialogComponent {
  shipmentComponent: IShipmentComponent;

  constructor(
    protected shipmentComponentService: ShipmentComponentService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.shipmentComponentService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'shipmentComponentListModification',
        content: 'Deleted an shipmentComponent'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'ko-shipment-component-delete-popup',
  template: ''
})
export class ShipmentComponentDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ shipmentComponent }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ShipmentComponentDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.shipmentComponent = shipmentComponent;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/shipment-component', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/shipment-component', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
