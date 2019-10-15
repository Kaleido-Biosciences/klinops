/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { KlinopsTestModule } from '../../../test.module';
import { ClinicalStudyDetailComponent } from 'app/entities/clinical-study/clinical-study-detail.component';
import { ClinicalStudy } from 'app/shared/model/clinical-study.model';

describe('Component Tests', () => {
  describe('ClinicalStudy Management Detail Component', () => {
    let comp: ClinicalStudyDetailComponent;
    let fixture: ComponentFixture<ClinicalStudyDetailComponent>;
    const route = ({ data: of({ clinicalStudy: new ClinicalStudy(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KlinopsTestModule],
        declarations: [ClinicalStudyDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ClinicalStudyDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ClinicalStudyDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.clinicalStudy).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
