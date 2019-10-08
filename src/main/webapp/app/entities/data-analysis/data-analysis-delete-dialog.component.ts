import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDataAnalysis } from 'app/shared/model/data-analysis.model';
import { DataAnalysisService } from './data-analysis.service';

@Component({
  selector: 'ko-data-analysis-delete-dialog',
  templateUrl: './data-analysis-delete-dialog.component.html'
})
export class DataAnalysisDeleteDialogComponent {
  dataAnalysis: IDataAnalysis;

  constructor(
    protected dataAnalysisService: DataAnalysisService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.dataAnalysisService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'dataAnalysisListModification',
        content: 'Deleted an dataAnalysis'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'ko-data-analysis-delete-popup',
  template: ''
})
export class DataAnalysisDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ dataAnalysis }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(DataAnalysisDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.dataAnalysis = dataAnalysis;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/data-analysis', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/data-analysis', { outlets: { popup: null } }]);
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
