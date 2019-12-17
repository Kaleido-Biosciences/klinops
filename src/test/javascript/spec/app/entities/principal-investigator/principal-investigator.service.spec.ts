import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import { PrincipalInvestigatorService } from 'app/entities/principal-investigator/principal-investigator.service';
import { IPrincipalInvestigator, PrincipalInvestigator } from 'app/shared/model/principal-investigator.model';

describe('Service Tests', () => {
  describe('PrincipalInvestigator Service', () => {
    let injector: TestBed;
    let service: PrincipalInvestigatorService;
    let httpMock: HttpTestingController;
    let elemDefault: IPrincipalInvestigator;
    let expectedResult;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(PrincipalInvestigatorService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new PrincipalInvestigator(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA');
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

      it('should create a PrincipalInvestigator', () => {
        const returnedFromService = Object.assign(
          {
            id: 0
          },
          elemDefault
        );
        const expected = Object.assign({}, returnedFromService);
        service
          .create(new PrincipalInvestigator(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a PrincipalInvestigator', () => {
        const returnedFromService = Object.assign(
          {
            investigatorName: 'BBBBBB',
            streetAddress: 'BBBBBB',
            city: 'BBBBBB',
            state: 'BBBBBB',
            zip: 'BBBBBB',
            country: 'BBBBBB',
            email: 'BBBBBB',
            phoneNumber: 'BBBBBB'
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

      it('should return a list of PrincipalInvestigator', () => {
        const returnedFromService = Object.assign(
          {
            investigatorName: 'BBBBBB',
            streetAddress: 'BBBBBB',
            city: 'BBBBBB',
            state: 'BBBBBB',
            zip: 'BBBBBB',
            country: 'BBBBBB',
            email: 'BBBBBB',
            phoneNumber: 'BBBBBB'
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

      it('should delete a PrincipalInvestigator', () => {
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
