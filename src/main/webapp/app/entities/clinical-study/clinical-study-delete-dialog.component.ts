/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IClinicalStudy } from 'app/shared/model/clinical-study.model';
import { ClinicalStudyService } from './clinical-study.service';

@Component({
  selector: 'ko-clinical-study-delete-dialog',
  templateUrl: './clinical-study-delete-dialog.component.html'
})
export class ClinicalStudyDeleteDialogComponent {
  clinicalStudy: IClinicalStudy;

  constructor(
    protected clinicalStudyService: ClinicalStudyService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.clinicalStudyService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'clinicalStudyListModification',
        content: 'Deleted an clinicalStudy'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'ko-clinical-study-delete-popup',
  template: ''
})
export class ClinicalStudyDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ clinicalStudy }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ClinicalStudyDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.clinicalStudy = clinicalStudy;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/clinical-study', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/clinical-study', { outlets: { popup: null } }]);
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
