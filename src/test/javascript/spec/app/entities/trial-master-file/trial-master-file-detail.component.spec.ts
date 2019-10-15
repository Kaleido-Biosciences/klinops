/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { KlinopsTestModule } from '../../../test.module';
import { TrialMasterFileDetailComponent } from 'app/entities/trial-master-file/trial-master-file-detail.component';
import { TrialMasterFile } from 'app/shared/model/trial-master-file.model';

describe('Component Tests', () => {
  describe('TrialMasterFile Management Detail Component', () => {
    let comp: TrialMasterFileDetailComponent;
    let fixture: ComponentFixture<TrialMasterFileDetailComponent>;
    const route = ({ data: of({ trialMasterFile: new TrialMasterFile(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KlinopsTestModule],
        declarations: [TrialMasterFileDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(TrialMasterFileDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TrialMasterFileDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.trialMasterFile).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
