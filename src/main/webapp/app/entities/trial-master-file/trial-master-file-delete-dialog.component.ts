/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITrialMasterFile } from 'app/shared/model/trial-master-file.model';
import { TrialMasterFileService } from './trial-master-file.service';

@Component({
  selector: 'ko-trial-master-file-delete-dialog',
  templateUrl: './trial-master-file-delete-dialog.component.html'
})
export class TrialMasterFileDeleteDialogComponent {
  trialMasterFile: ITrialMasterFile;

  constructor(
    protected trialMasterFileService: TrialMasterFileService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.trialMasterFileService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'trialMasterFileListModification',
        content: 'Deleted an trialMasterFile'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'ko-trial-master-file-delete-popup',
  template: ''
})
export class TrialMasterFileDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ trialMasterFile }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(TrialMasterFileDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.trialMasterFile = trialMasterFile;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/trial-master-file', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/trial-master-file', { outlets: { popup: null } }]);
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
