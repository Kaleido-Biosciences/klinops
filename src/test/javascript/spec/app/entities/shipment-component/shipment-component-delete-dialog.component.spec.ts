/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { KlinopsTestModule } from '../../../test.module';
import { ShipmentComponentDeleteDialogComponent } from 'app/entities/shipment-component/shipment-component-delete-dialog.component';
import { ShipmentComponentService } from 'app/entities/shipment-component/shipment-component.service';

describe('Component Tests', () => {
  describe('ShipmentComponent Management Delete Component', () => {
    let comp: ShipmentComponentDeleteDialogComponent;
    let fixture: ComponentFixture<ShipmentComponentDeleteDialogComponent>;
    let service: ShipmentComponentService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KlinopsTestModule],
        declarations: [ShipmentComponentDeleteDialogComponent]
      })
        .overrideTemplate(ShipmentComponentDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ShipmentComponentDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ShipmentComponentService);
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
