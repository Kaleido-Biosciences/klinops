import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { KlinopsTestModule } from '../../../test.module';
import { BioAnalysisDeleteDialogComponent } from 'app/entities/bio-analysis/bio-analysis-delete-dialog.component';
import { BioAnalysisService } from 'app/entities/bio-analysis/bio-analysis.service';

describe('Component Tests', () => {
  describe('BioAnalysis Management Delete Component', () => {
    let comp: BioAnalysisDeleteDialogComponent;
    let fixture: ComponentFixture<BioAnalysisDeleteDialogComponent>;
    let service: BioAnalysisService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KlinopsTestModule],
        declarations: [BioAnalysisDeleteDialogComponent]
      })
        .overrideTemplate(BioAnalysisDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BioAnalysisDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BioAnalysisService);
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
