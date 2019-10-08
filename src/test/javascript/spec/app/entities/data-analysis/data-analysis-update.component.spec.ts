import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { KlinopsTestModule } from '../../../test.module';
import { DataAnalysisUpdateComponent } from 'app/entities/data-analysis/data-analysis-update.component';
import { DataAnalysisService } from 'app/entities/data-analysis/data-analysis.service';
import { DataAnalysis } from 'app/shared/model/data-analysis.model';

describe('Component Tests', () => {
  describe('DataAnalysis Management Update Component', () => {
    let comp: DataAnalysisUpdateComponent;
    let fixture: ComponentFixture<DataAnalysisUpdateComponent>;
    let service: DataAnalysisService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KlinopsTestModule],
        declarations: [DataAnalysisUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(DataAnalysisUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DataAnalysisUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DataAnalysisService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new DataAnalysis(123);
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
        const entity = new DataAnalysis();
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
