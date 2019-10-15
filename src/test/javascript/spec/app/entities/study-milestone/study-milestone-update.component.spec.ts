/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { KlinopsTestModule } from '../../../test.module';
import { StudyMilestoneUpdateComponent } from 'app/entities/study-milestone/study-milestone-update.component';
import { StudyMilestoneService } from 'app/entities/study-milestone/study-milestone.service';
import { StudyMilestone } from 'app/shared/model/study-milestone.model';

describe('Component Tests', () => {
  describe('StudyMilestone Management Update Component', () => {
    let comp: StudyMilestoneUpdateComponent;
    let fixture: ComponentFixture<StudyMilestoneUpdateComponent>;
    let service: StudyMilestoneService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KlinopsTestModule],
        declarations: [StudyMilestoneUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(StudyMilestoneUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StudyMilestoneUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(StudyMilestoneService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new StudyMilestone(123);
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
        const entity = new StudyMilestone();
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
