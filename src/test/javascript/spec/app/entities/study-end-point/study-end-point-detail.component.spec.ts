/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { KlinopsTestModule } from '../../../test.module';
import { StudyEndPointDetailComponent } from 'app/entities/study-end-point/study-end-point-detail.component';
import { StudyEndPoint } from 'app/shared/model/study-end-point.model';

describe('Component Tests', () => {
  describe('StudyEndPoint Management Detail Component', () => {
    let comp: StudyEndPointDetailComponent;
    let fixture: ComponentFixture<StudyEndPointDetailComponent>;
    const route = ({ data: of({ studyEndPoint: new StudyEndPoint(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KlinopsTestModule],
        declarations: [StudyEndPointDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(StudyEndPointDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(StudyEndPointDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.studyEndPoint).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
