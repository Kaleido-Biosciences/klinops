/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { StudyMilestoneService } from 'app/entities/study-milestone/study-milestone.service';
import { IStudyMilestone, StudyMilestone } from 'app/shared/model/study-milestone.model';

describe('Service Tests', () => {
  describe('StudyMilestone Service', () => {
    let injector: TestBed;
    let service: StudyMilestoneService;
    let httpMock: HttpTestingController;
    let elemDefault: IStudyMilestone;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(StudyMilestoneService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new StudyMilestone(0, 'AAAAAAA', 'AAAAAAA', currentDate, currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            projectedCompletionDate: currentDate.format(DATE_FORMAT),
            actualCompletionDate: currentDate.format(DATE_FORMAT)
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

      it('should create a StudyMilestone', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            projectedCompletionDate: currentDate.format(DATE_FORMAT),
            actualCompletionDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            projectedCompletionDate: currentDate,
            actualCompletionDate: currentDate
          },
          returnedFromService
        );
        service
          .create(new StudyMilestone(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a StudyMilestone', () => {
        const returnedFromService = Object.assign(
          {
            mileStoneName: 'BBBBBB',
            mileStoneType: 'BBBBBB',
            projectedCompletionDate: currentDate.format(DATE_FORMAT),
            actualCompletionDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            projectedCompletionDate: currentDate,
            actualCompletionDate: currentDate
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

      it('should return a list of StudyMilestone', () => {
        const returnedFromService = Object.assign(
          {
            mileStoneName: 'BBBBBB',
            mileStoneType: 'BBBBBB',
            projectedCompletionDate: currentDate.format(DATE_FORMAT),
            actualCompletionDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            projectedCompletionDate: currentDate,
            actualCompletionDate: currentDate
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

      it('should delete a StudyMilestone', () => {
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
