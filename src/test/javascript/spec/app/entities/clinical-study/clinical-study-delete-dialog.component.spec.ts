import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { KlinopsTestModule } from '../../../test.module';
import { ClinicalStudyDeleteDialogComponent } from 'app/entities/clinical-study/clinical-study-delete-dialog.component';
import { ClinicalStudyService } from 'app/entities/clinical-study/clinical-study.service';

describe('Component Tests', () => {
  describe('ClinicalStudy Management Delete Component', () => {
    let comp: ClinicalStudyDeleteDialogComponent;
    let fixture: ComponentFixture<ClinicalStudyDeleteDialogComponent>;
    let service: ClinicalStudyService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KlinopsTestModule],
        declarations: [ClinicalStudyDeleteDialogComponent]
      })
        .overrideTemplate(ClinicalStudyDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ClinicalStudyDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ClinicalStudyService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
