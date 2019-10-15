/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { KlinopsTestModule } from '../../../test.module';
import { StudySampleDeleteDialogComponent } from 'app/entities/study-sample/study-sample-delete-dialog.component';
import { StudySampleService } from 'app/entities/study-sample/study-sample.service';

describe('Component Tests', () => {
  describe('StudySample Management Delete Component', () => {
    let comp: StudySampleDeleteDialogComponent;
    let fixture: ComponentFixture<StudySampleDeleteDialogComponent>;
    let service: StudySampleService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KlinopsTestModule],
        declarations: [StudySampleDeleteDialogComponent]
      })
        .overrideTemplate(StudySampleDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(StudySampleDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(StudySampleService);
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
