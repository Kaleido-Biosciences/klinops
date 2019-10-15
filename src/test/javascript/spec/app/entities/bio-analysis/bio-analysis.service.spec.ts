/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { BioAnalysisService } from 'app/entities/bio-analysis/bio-analysis.service';
import { IBioAnalysis, BioAnalysis } from 'app/shared/model/bio-analysis.model';

describe('Service Tests', () => {
  describe('BioAnalysis Service', () => {
    let injector: TestBed;
    let service: BioAnalysisService;
    let httpMock: HttpTestingController;
    let elemDefault: IBioAnalysis;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(BioAnalysisService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new BioAnalysis(
        0,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        currentDate,
        currentDate,
        currentDate,
        currentDate,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA'
      );
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            anticipatedLabWorkStartDate: currentDate.format(DATE_FORMAT),
            actualLabWorkStartDate: currentDate.format(DATE_FORMAT),
            anticipatedLabResultDeliveryDate: currentDate.format(DATE_FORMAT),
            actualLabResultDeliveryDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should create a BioAnalysis', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            anticipatedLabWorkStartDate: currentDate.format(DATE_FORMAT),
            actualLabWorkStartDate: currentDate.format(DATE_FORMAT),
            anticipatedLabResultDeliveryDate: currentDate.format(DATE_FORMAT),
            actualLabResultDeliveryDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            anticipatedLabWorkStartDate: currentDate,
            actualLabWorkStartDate: currentDate,
            anticipatedLabResultDeliveryDate: currentDate,
            actualLabResultDeliveryDate: currentDate
          },
          returnedFromService
        );
        service
          .create(new BioAnalysis(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a BioAnalysis', () => {
        const returnedFromService = Object.assign(
          {
            analyte: 'BBBBBB',
            sampleType: 'BBBBBB',
            bioAnalysisType: 'BBBBBB',
            anticipatedLabWorkStartDate: currentDate.format(DATE_FORMAT),
            actualLabWorkStartDate: currentDate.format(DATE_FORMAT),
            anticipatedLabResultDeliveryDate: currentDate.format(DATE_FORMAT),
            actualLabResultDeliveryDate: currentDate.format(DATE_FORMAT),
            dataLocation: 'BBBBBB',
            contactName: 'BBBBBB',
            contactEmail: 'BBBBBB',
            comments: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            anticipatedLabWorkStartDate: currentDate,
            actualLabWorkStartDate: currentDate,
            anticipatedLabResultDeliveryDate: currentDate,
            actualLabResultDeliveryDate: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should return a list of BioAnalysis', () => {
        const returnedFromService = Object.assign(
          {
            analyte: 'BBBBBB',
            sampleType: 'BBBBBB',
            bioAnalysisType: 'BBBBBB',
            anticipatedLabWorkStartDate: currentDate.format(DATE_FORMAT),
            actualLabWorkStartDate: currentDate.format(DATE_FORMAT),
            anticipatedLabResultDeliveryDate: currentDate.format(DATE_FORMAT),
            actualLabResultDeliveryDate: currentDate.format(DATE_FORMAT),
            dataLocation: 'BBBBBB',
            contactName: 'BBBBBB',
            contactEmail: 'BBBBBB',
            comments: 'BBBBBB'
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            anticipatedLabWorkStartDate: currentDate,
            actualLabWorkStartDate: currentDate,
            anticipatedLabResultDeliveryDate: currentDate,
            actualLabResultDeliveryDate: currentDate
          },
          returnedFromService
        );
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

      it('should delete a BioAnalysis', () => {
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
