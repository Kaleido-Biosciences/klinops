/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { KlinopsTestModule } from '../../../test.module';
import { StudySampleDetailComponent } from 'app/entities/study-sample/study-sample-detail.component';
import { StudySample } from 'app/shared/model/study-sample.model';

describe('Component Tests', () => {
  describe('StudySample Management Detail Component', () => {
    let comp: StudySampleDetailComponent;
    let fixture: ComponentFixture<StudySampleDetailComponent>;
    const route = ({ data: of({ studySample: new StudySample(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KlinopsTestModule],
        declarations: [StudySampleDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(StudySampleDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(StudySampleDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.studySample).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
