import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IStudyEndPoint } from 'app/shared/model/study-end-point.model';
import { StudyEndPointService } from './study-end-point.service';

@Component({
  selector: 'ko-study-end-point-delete-dialog',
  templateUrl: './study-end-point-delete-dialog.component.html'
})
export class StudyEndPointDeleteDialogComponent {
  studyEndPoint: IStudyEndPoint;

  constructor(
    protected studyEndPointService: StudyEndPointService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.studyEndPointService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'studyEndPointListModification',
        content: 'Deleted an studyEndPoint'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'ko-study-end-point-delete-popup',
  template: ''
})
export class StudyEndPointDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ studyEndPoint }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(StudyEndPointDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.studyEndPoint = studyEndPoint;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/study-end-point', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/study-end-point', { outlets: { popup: null } }]);
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
