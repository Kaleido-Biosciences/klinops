/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { KlinopsTestModule } from '../../../test.module';
import { StudyMilestoneDeleteDialogComponent } from 'app/entities/study-milestone/study-milestone-delete-dialog.component';
import { StudyMilestoneService } from 'app/entities/study-milestone/study-milestone.service';

describe('Component Tests', () => {
  describe('StudyMilestone Management Delete Component', () => {
    let comp: StudyMilestoneDeleteDialogComponent;
    let fixture: ComponentFixture<StudyMilestoneDeleteDialogComponent>;
    let service: StudyMilestoneService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KlinopsTestModule],
        declarations: [StudyMilestoneDeleteDialogComponent]
      })
        .overrideTemplate(StudyMilestoneDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(StudyMilestoneDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(StudyMilestoneService);
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
