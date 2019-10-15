/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { KlinopsTestModule } from '../../../test.module';
import { ShipmentComponentDetailComponent } from 'app/entities/shipment-component/shipment-component-detail.component';
import { ShipmentComponent } from 'app/shared/model/shipment-component.model';

describe('Component Tests', () => {
  describe('ShipmentComponent Management Detail Component', () => {
    let comp: ShipmentComponentDetailComponent;
    let fixture: ComponentFixture<ShipmentComponentDetailComponent>;
    const route = ({ data: of({ shipmentComponent: new ShipmentComponent(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KlinopsTestModule],
        declarations: [ShipmentComponentDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ShipmentComponentDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ShipmentComponentDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.shipmentComponent).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
