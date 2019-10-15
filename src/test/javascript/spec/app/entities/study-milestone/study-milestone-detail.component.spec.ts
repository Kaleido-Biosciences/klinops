/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { KlinopsTestModule } from '../../../test.module';
import { StudyMilestoneDetailComponent } from 'app/entities/study-milestone/study-milestone-detail.component';
import { StudyMilestone } from 'app/shared/model/study-milestone.model';

describe('Component Tests', () => {
  describe('StudyMilestone Management Detail Component', () => {
    let comp: StudyMilestoneDetailComponent;
    let fixture: ComponentFixture<StudyMilestoneDetailComponent>;
    const route = ({ data: of({ studyMilestone: new StudyMilestone(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KlinopsTestModule],
        declarations: [StudyMilestoneDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(StudyMilestoneDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(StudyMilestoneDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.studyMilestone).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
