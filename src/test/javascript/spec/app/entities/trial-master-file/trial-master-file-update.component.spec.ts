import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { KlinopsTestModule } from '../../../test.module';
import { TrialMasterFileUpdateComponent } from 'app/entities/trial-master-file/trial-master-file-update.component';
import { TrialMasterFileService } from 'app/entities/trial-master-file/trial-master-file.service';
import { TrialMasterFile } from 'app/shared/model/trial-master-file.model';

describe('Component Tests', () => {
  describe('TrialMasterFile Management Update Component', () => {
    let comp: TrialMasterFileUpdateComponent;
    let fixture: ComponentFixture<TrialMasterFileUpdateComponent>;
    let service: TrialMasterFileService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KlinopsTestModule],
        declarations: [TrialMasterFileUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(TrialMasterFileUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TrialMasterFileUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TrialMasterFileService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new TrialMasterFile(123);
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
        const entity = new TrialMasterFile();
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
