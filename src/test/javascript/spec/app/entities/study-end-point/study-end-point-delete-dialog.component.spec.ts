import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { KlinopsTestModule } from '../../../test.module';
import { StudyEndPointDeleteDialogComponent } from 'app/entities/study-end-point/study-end-point-delete-dialog.component';
import { StudyEndPointService } from 'app/entities/study-end-point/study-end-point.service';

describe('Component Tests', () => {
  describe('StudyEndPoint Management Delete Component', () => {
    let comp: StudyEndPointDeleteDialogComponent;
    let fixture: ComponentFixture<StudyEndPointDeleteDialogComponent>;
    let service: StudyEndPointService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KlinopsTestModule],
        declarations: [StudyEndPointDeleteDialogComponent]
      })
        .overrideTemplate(StudyEndPointDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(StudyEndPointDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(StudyEndPointService);
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
