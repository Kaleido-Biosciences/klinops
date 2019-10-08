import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IStudySample } from 'app/shared/model/study-sample.model';
import { StudySampleService } from './study-sample.service';

@Component({
  selector: 'ko-study-sample-delete-dialog',
  templateUrl: './study-sample-delete-dialog.component.html'
})
export class StudySampleDeleteDialogComponent {
  studySample: IStudySample;

  constructor(
    protected studySampleService: StudySampleService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.studySampleService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'studySampleListModification',
        content: 'Deleted an studySample'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'ko-study-sample-delete-popup',
  template: ''
})
export class StudySampleDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ studySample }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(StudySampleDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.studySample = studySample;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/study-sample', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/study-sample', { outlets: { popup: null } }]);
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
