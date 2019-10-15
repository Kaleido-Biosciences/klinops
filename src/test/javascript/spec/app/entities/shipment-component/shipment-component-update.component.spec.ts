/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { KlinopsTestModule } from '../../../test.module';
import { ShipmentComponentUpdateComponent } from 'app/entities/shipment-component/shipment-component-update.component';
import { ShipmentComponentService } from 'app/entities/shipment-component/shipment-component.service';
import { ShipmentComponent } from 'app/shared/model/shipment-component.model';

describe('Component Tests', () => {
  describe('ShipmentComponent Management Update Component', () => {
    let comp: ShipmentComponentUpdateComponent;
    let fixture: ComponentFixture<ShipmentComponentUpdateComponent>;
    let service: ShipmentComponentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KlinopsTestModule],
        declarations: [ShipmentComponentUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ShipmentComponentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ShipmentComponentUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ShipmentComponentService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ShipmentComponent(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new ShipmentComponent();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
