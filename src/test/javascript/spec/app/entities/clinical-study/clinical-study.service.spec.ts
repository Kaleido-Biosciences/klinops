/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import { ClinicalStudyService } from 'app/entities/clinical-study/clinical-study.service';
import { IClinicalStudy, ClinicalStudy } from 'app/shared/model/clinical-study.model';

describe('Service Tests', () => {
  describe('ClinicalStudy Service', () => {
    let injector: TestBed;
    let service: ClinicalStudyService;
    let httpMock: HttpTestingController;
    let elemDefault: IClinicalStudy;
    let expectedResult;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(ClinicalStudyService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new ClinicalStudy(
        0,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        0,
        0,
        'AAAAAAA',
        'AAAAAAA',
        0,
        0,
        'AAAAAAA',
        0,
        0,
        0,
        false,
        false,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA'
      );
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should create a ClinicalStudy', () => {
        const returnedFromService = Object.assign(
          {
            id: 0
          },
          elemDefault
        );
        const expected = Object.assign({}, returnedFromService);
        service
          .create(new ClinicalStudy(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a ClinicalStudy', () => {
        const returnedFromService = Object.assign(
          {
            studyIdentifier: 'BBBBBB',
            phase: 'BBBBBB',
            status: 'BBBBBB',
            sequence: 1,
            studyYear: 1,
            name: 'BBBBBB',
            design: 'BBBBBB',
            numberOfCohorts: 1,
            intendedSubjectsPerCohort: 1,
            populationDiseaseState: 'BBBBBB',
            minimumAge: 1,
            maximumAge: 1,
            subjectsEnrolled: 1,
            femalesEligible: true,
            malesEligible: true,
            studyShortName: 'BBBBBB',
            projectManager: 'BBBBBB',
            principalPhysician: 'BBBBBB',
            researchRepresentative: 'BBBBBB',
            analysisRepresentative: 'BBBBBB',
            dataManager: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should return a list of ClinicalStudy', () => {
        const returnedFromService = Object.assign(
          {
            studyIdentifier: 'BBBBBB',
            phase: 'BBBBBB',
            status: 'BBBBBB',
            sequence: 1,
            studyYear: 1,
            name: 'BBBBBB',
            design: 'BBBBBB',
            numberOfCohorts: 1,
            intendedSubjectsPerCohort: 1,
            populationDiseaseState: 'BBBBBB',
            minimumAge: 1,
            maximumAge: 1,
            subjectsEnrolled: 1,
            femalesEligible: true,
            malesEligible: true,
            studyShortName: 'BBBBBB',
            projectManager: 'BBBBBB',
            principalPhysician: 'BBBBBB',
            researchRepresentative: 'BBBBBB',
            analysisRepresentative: 'BBBBBB',
            dataManager: 'BBBBBB'
          },
          elemDefault
        );
        const expected = Object.assign({}, returnedFromService);
        service
          .query(expected)
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a ClinicalStudy', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
