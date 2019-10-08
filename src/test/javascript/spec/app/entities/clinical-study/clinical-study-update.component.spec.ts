import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { KlinopsTestModule } from '../../../test.module';
import { ClinicalStudyUpdateComponent } from 'app/entities/clinical-study/clinical-study-update.component';
import { ClinicalStudyService } from 'app/entities/clinical-study/clinical-study.service';
import { ClinicalStudy } from 'app/shared/model/clinical-study.model';

describe('Component Tests', () => {
  describe('ClinicalStudy Management Update Component', () => {
    let comp: ClinicalStudyUpdateComponent;
    let fixture: ComponentFixture<ClinicalStudyUpdateComponent>;
    let service: ClinicalStudyService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KlinopsTestModule],
        declarations: [ClinicalStudyUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ClinicalStudyUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ClinicalStudyUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ClinicalStudyService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ClinicalStudy(123);
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
        const entity = new ClinicalStudy();
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
