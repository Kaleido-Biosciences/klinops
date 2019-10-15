/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { KlinopsTestModule } from '../../../test.module';
import { BioAnalysisUpdateComponent } from 'app/entities/bio-analysis/bio-analysis-update.component';
import { BioAnalysisService } from 'app/entities/bio-analysis/bio-analysis.service';
import { BioAnalysis } from 'app/shared/model/bio-analysis.model';

describe('Component Tests', () => {
  describe('BioAnalysis Management Update Component', () => {
    let comp: BioAnalysisUpdateComponent;
    let fixture: ComponentFixture<BioAnalysisUpdateComponent>;
    let service: BioAnalysisService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KlinopsTestModule],
        declarations: [BioAnalysisUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(BioAnalysisUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BioAnalysisUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BioAnalysisService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new BioAnalysis(123);
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
        const entity = new BioAnalysis();
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
