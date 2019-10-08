import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import { LaboratoryService } from 'app/entities/laboratory/laboratory.service';
import { ILaboratory, Laboratory } from 'app/shared/model/laboratory.model';

describe('Service Tests', () => {
  describe('Laboratory Service', () => {
    let injector: TestBed;
    let service: LaboratoryService;
    let httpMock: HttpTestingController;
    let elemDefault: ILaboratory;
    let expectedResult;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(LaboratoryService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new Laboratory(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA');
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

      it('should create a Laboratory', () => {
        const returnedFromService = Object.assign(
          {
            id: 0
          },
          elemDefault
        );
        const expected = Object.assign({}, returnedFromService);
        service
          .create(new Laboratory(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a Laboratory', () => {
        const returnedFromService = Object.assign(
          {
            labName: 'BBBBBB',
            streetAddress: 'BBBBBB',
            city: 'BBBBBB',
            state: 'BBBBBB',
            zip: 'BBBBBB',
            country: 'BBBBBB',
            labContactEmail: 'BBBBBB',
            labContactName: 'BBBBBB',
            labContactPhoneNumber: 'BBBBBB'
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

      it('should return a list of Laboratory', () => {
        const returnedFromService = Object.assign(
          {
            labName: 'BBBBBB',
            streetAddress: 'BBBBBB',
            city: 'BBBBBB',
            state: 'BBBBBB',
            zip: 'BBBBBB',
            country: 'BBBBBB',
            labContactEmail: 'BBBBBB',
            labContactName: 'BBBBBB',
            labContactPhoneNumber: 'BBBBBB'
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

      it('should delete a Laboratory', () => {
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
