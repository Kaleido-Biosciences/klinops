/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { KlinopsTestModule } from '../../../test.module';
import { BioAnalysisDetailComponent } from 'app/entities/bio-analysis/bio-analysis-detail.component';
import { BioAnalysis } from 'app/shared/model/bio-analysis.model';

describe('Component Tests', () => {
  describe('BioAnalysis Management Detail Component', () => {
    let comp: BioAnalysisDetailComponent;
    let fixture: ComponentFixture<BioAnalysisDetailComponent>;
    const route = ({ data: of({ bioAnalysis: new BioAnalysis(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KlinopsTestModule],
        declarations: [BioAnalysisDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(BioAnalysisDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BioAnalysisDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.bioAnalysis).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
