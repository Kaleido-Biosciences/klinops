import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPrincipalInvestigator } from 'app/shared/model/principal-investigator.model';
import { PrincipalInvestigatorService } from './principal-investigator.service';

@Component({
  selector: 'ko-principal-investigator-delete-dialog',
  templateUrl: './principal-investigator-delete-dialog.component.html'
})
export class PrincipalInvestigatorDeleteDialogComponent {
  principalInvestigator: IPrincipalInvestigator;

  constructor(
    protected principalInvestigatorService: PrincipalInvestigatorService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.principalInvestigatorService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'principalInvestigatorListModification',
        content: 'Deleted an principalInvestigator'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'ko-principal-investigator-delete-popup',
  template: ''
})
export class PrincipalInvestigatorDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ principalInvestigator }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(PrincipalInvestigatorDeleteDialogComponent as Component, {
          size: 'lg',
          backdrop: 'static'
        });
        this.ngbModalRef.componentInstance.principalInvestigator = principalInvestigator;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/principal-investigator', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/principal-investigator', { outlets: { popup: null } }]);
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
