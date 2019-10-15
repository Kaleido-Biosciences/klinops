/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { KlinopsTestModule } from '../../../test.module';
import { StudyEndPointUpdateComponent } from 'app/entities/study-end-point/study-end-point-update.component';
import { StudyEndPointService } from 'app/entities/study-end-point/study-end-point.service';
import { StudyEndPoint } from 'app/shared/model/study-end-point.model';

describe('Component Tests', () => {
  describe('StudyEndPoint Management Update Component', () => {
    let comp: StudyEndPointUpdateComponent;
    let fixture: ComponentFixture<StudyEndPointUpdateComponent>;
    let service: StudyEndPointService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KlinopsTestModule],
        declarations: [StudyEndPointUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(StudyEndPointUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StudyEndPointUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(StudyEndPointService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new StudyEndPoint(123);
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
        const entity = new StudyEndPoint();
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
