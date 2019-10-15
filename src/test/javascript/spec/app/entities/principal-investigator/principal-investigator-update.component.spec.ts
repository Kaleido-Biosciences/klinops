/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { KlinopsTestModule } from '../../../test.module';
import { PrincipalInvestigatorUpdateComponent } from 'app/entities/principal-investigator/principal-investigator-update.component';
import { PrincipalInvestigatorService } from 'app/entities/principal-investigator/principal-investigator.service';
import { PrincipalInvestigator } from 'app/shared/model/principal-investigator.model';

describe('Component Tests', () => {
  describe('PrincipalInvestigator Management Update Component', () => {
    let comp: PrincipalInvestigatorUpdateComponent;
    let fixture: ComponentFixture<PrincipalInvestigatorUpdateComponent>;
    let service: PrincipalInvestigatorService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KlinopsTestModule],
        declarations: [PrincipalInvestigatorUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(PrincipalInvestigatorUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PrincipalInvestigatorUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PrincipalInvestigatorService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new PrincipalInvestigator(123);
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
        const entity = new PrincipalInvestigator();
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
