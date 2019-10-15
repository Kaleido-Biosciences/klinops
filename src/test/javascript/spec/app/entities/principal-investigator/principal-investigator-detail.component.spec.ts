/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { KlinopsTestModule } from '../../../test.module';
import { PrincipalInvestigatorDetailComponent } from 'app/entities/principal-investigator/principal-investigator-detail.component';
import { PrincipalInvestigator } from 'app/shared/model/principal-investigator.model';

describe('Component Tests', () => {
  describe('PrincipalInvestigator Management Detail Component', () => {
    let comp: PrincipalInvestigatorDetailComponent;
    let fixture: ComponentFixture<PrincipalInvestigatorDetailComponent>;
    const route = ({ data: of({ principalInvestigator: new PrincipalInvestigator(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KlinopsTestModule],
        declarations: [PrincipalInvestigatorDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(PrincipalInvestigatorDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PrincipalInvestigatorDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.principalInvestigator).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
