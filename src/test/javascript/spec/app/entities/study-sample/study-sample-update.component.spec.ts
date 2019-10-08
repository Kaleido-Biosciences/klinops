import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { KlinopsTestModule } from '../../../test.module';
import { StudySampleUpdateComponent } from 'app/entities/study-sample/study-sample-update.component';
import { StudySampleService } from 'app/entities/study-sample/study-sample.service';
import { StudySample } from 'app/shared/model/study-sample.model';

describe('Component Tests', () => {
  describe('StudySample Management Update Component', () => {
    let comp: StudySampleUpdateComponent;
    let fixture: ComponentFixture<StudySampleUpdateComponent>;
    let service: StudySampleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KlinopsTestModule],
        declarations: [StudySampleUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(StudySampleUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StudySampleUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(StudySampleService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new StudySample(123);
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
        const entity = new StudySample();
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
