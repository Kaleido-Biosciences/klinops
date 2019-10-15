/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { KlinopsTestModule } from '../../../test.module';
import { DataAnalysisDetailComponent } from 'app/entities/data-analysis/data-analysis-detail.component';
import { DataAnalysis } from 'app/shared/model/data-analysis.model';

describe('Component Tests', () => {
  describe('DataAnalysis Management Detail Component', () => {
    let comp: DataAnalysisDetailComponent;
    let fixture: ComponentFixture<DataAnalysisDetailComponent>;
    const route = ({ data: of({ dataAnalysis: new DataAnalysis(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KlinopsTestModule],
        declarations: [DataAnalysisDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(DataAnalysisDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DataAnalysisDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.dataAnalysis).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
