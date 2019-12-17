import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IStudyMilestone } from 'app/shared/model/study-milestone.model';
import { StudyMilestoneService } from './study-milestone.service';

@Component({
  selector: 'ko-study-milestone-delete-dialog',
  templateUrl: './study-milestone-delete-dialog.component.html'
})
export class StudyMilestoneDeleteDialogComponent {
  studyMilestone: IStudyMilestone;

  constructor(
    protected studyMilestoneService: StudyMilestoneService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.studyMilestoneService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'studyMilestoneListModification',
        content: 'Deleted an studyMilestone'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'ko-study-milestone-delete-popup',
  template: ''
})
export class StudyMilestoneDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ studyMilestone }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(StudyMilestoneDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.studyMilestone = studyMilestone;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/study-milestone', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/study-milestone', { outlets: { popup: null } }]);
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
