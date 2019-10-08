import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { KlinopsTestModule } from '../../../test.module';
import { DataAnalysisDeleteDialogComponent } from 'app/entities/data-analysis/data-analysis-delete-dialog.component';
import { DataAnalysisService } from 'app/entities/data-analysis/data-analysis.service';

describe('Component Tests', () => {
  describe('DataAnalysis Management Delete Component', () => {
    let comp: DataAnalysisDeleteDialogComponent;
    let fixture: ComponentFixture<DataAnalysisDeleteDialogComponent>;
    let service: DataAnalysisService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KlinopsTestModule],
        declarations: [DataAnalysisDeleteDialogComponent]
      })
        .overrideTemplate(DataAnalysisDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DataAnalysisDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DataAnalysisService);
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
