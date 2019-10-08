import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { DataAnalysisService } from 'app/entities/data-analysis/data-analysis.service';
import { IDataAnalysis, DataAnalysis } from 'app/shared/model/data-analysis.model';

describe('Service Tests', () => {
  describe('DataAnalysis Service', () => {
    let injector: TestBed;
    let service: DataAnalysisService;
    let httpMock: HttpTestingController;
    let elemDefault: IDataAnalysis;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(DataAnalysisService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new DataAnalysis(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', currentDate, currentDate, 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            anticipatedAnalysisDeliveryDate: currentDate.format(DATE_FORMAT),
            actualAnalysisDeliveryDate: currentDate.format(DATE_FORMAT)
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

      it('should create a DataAnalysis', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            anticipatedAnalysisDeliveryDate: currentDate.format(DATE_FORMAT),
            actualAnalysisDeliveryDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            anticipatedAnalysisDeliveryDate: currentDate,
            actualAnalysisDeliveryDate: currentDate
          },
          returnedFromService
        );
        service
          .create(new DataAnalysis(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a DataAnalysis', () => {
        const returnedFromService = Object.assign(
          {
            dataAnalysesType: 'BBBBBB',
            contactName: 'BBBBBB',
            contactEmail: 'BBBBBB',
            anticipatedAnalysisDeliveryDate: currentDate.format(DATE_FORMAT),
            actualAnalysisDeliveryDate: currentDate.format(DATE_FORMAT),
            dataLocation: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            anticipatedAnalysisDeliveryDate: currentDate,
            actualAnalysisDeliveryDate: currentDate
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

      it('should return a list of DataAnalysis', () => {
        const returnedFromService = Object.assign(
          {
            dataAnalysesType: 'BBBBBB',
            contactName: 'BBBBBB',
            contactEmail: 'BBBBBB',
            anticipatedAnalysisDeliveryDate: currentDate.format(DATE_FORMAT),
            actualAnalysisDeliveryDate: currentDate.format(DATE_FORMAT),
            dataLocation: 'BBBBBB'
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            anticipatedAnalysisDeliveryDate: currentDate,
            actualAnalysisDeliveryDate: currentDate
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

      it('should delete a DataAnalysis', () => {
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
