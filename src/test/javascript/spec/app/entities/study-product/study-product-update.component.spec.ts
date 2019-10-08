import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { KlinopsTestModule } from '../../../test.module';
import { StudyProductUpdateComponent } from 'app/entities/study-product/study-product-update.component';
import { StudyProductService } from 'app/entities/study-product/study-product.service';
import { StudyProduct } from 'app/shared/model/study-product.model';

describe('Component Tests', () => {
  describe('StudyProduct Management Update Component', () => {
    let comp: StudyProductUpdateComponent;
    let fixture: ComponentFixture<StudyProductUpdateComponent>;
    let service: StudyProductService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KlinopsTestModule],
        declarations: [StudyProductUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(StudyProductUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StudyProductUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(StudyProductService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new StudyProduct(123);
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
        const entity = new StudyProduct();
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
