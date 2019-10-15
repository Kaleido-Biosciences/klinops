/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBioAnalysis } from 'app/shared/model/bio-analysis.model';
import { BioAnalysisService } from './bio-analysis.service';

@Component({
  selector: 'ko-bio-analysis-delete-dialog',
  templateUrl: './bio-analysis-delete-dialog.component.html'
})
export class BioAnalysisDeleteDialogComponent {
  bioAnalysis: IBioAnalysis;

  constructor(
    protected bioAnalysisService: BioAnalysisService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.bioAnalysisService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'bioAnalysisListModification',
        content: 'Deleted an bioAnalysis'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'ko-bio-analysis-delete-popup',
  template: ''
})
export class BioAnalysisDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ bioAnalysis }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(BioAnalysisDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.bioAnalysis = bioAnalysis;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/bio-analysis', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/bio-analysis', { outlets: { popup: null } }]);
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
