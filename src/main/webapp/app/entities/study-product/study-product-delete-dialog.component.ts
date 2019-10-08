import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IStudyProduct } from 'app/shared/model/study-product.model';
import { StudyProductService } from './study-product.service';

@Component({
  selector: 'ko-study-product-delete-dialog',
  templateUrl: './study-product-delete-dialog.component.html'
})
export class StudyProductDeleteDialogComponent {
  studyProduct: IStudyProduct;

  constructor(
    protected studyProductService: StudyProductService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.studyProductService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'studyProductListModification',
        content: 'Deleted an studyProduct'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'ko-study-product-delete-popup',
  template: ''
})
export class StudyProductDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ studyProduct }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(StudyProductDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.studyProduct = studyProduct;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/study-product', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/study-product', { outlets: { popup: null } }]);
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
