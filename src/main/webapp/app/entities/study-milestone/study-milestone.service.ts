/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IStudyMilestone } from 'app/shared/model/study-milestone.model';

type EntityResponseType = HttpResponse<IStudyMilestone>;
type EntityArrayResponseType = HttpResponse<IStudyMilestone[]>;

@Injectable({ providedIn: 'root' })
export class StudyMilestoneService {
  public resourceUrl = SERVER_API_URL + 'api/study-milestones';

  constructor(protected http: HttpClient) {}

  create(studyMilestone: IStudyMilestone): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(studyMilestone);
    return this.http
      .post<IStudyMilestone>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(studyMilestone: IStudyMilestone): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(studyMilestone);
    return this.http
      .put<IStudyMilestone>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IStudyMilestone>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IStudyMilestone[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(studyMilestone: IStudyMilestone): IStudyMilestone {
    const copy: IStudyMilestone = Object.assign({}, studyMilestone, {
      projectedCompletionDate:
        studyMilestone.projectedCompletionDate != null && studyMilestone.projectedCompletionDate.isValid()
          ? studyMilestone.projectedCompletionDate.format(DATE_FORMAT)
          : null,
      actualCompletionDate:
        studyMilestone.actualCompletionDate != null && studyMilestone.actualCompletionDate.isValid()
          ? studyMilestone.actualCompletionDate.format(DATE_FORMAT)
          : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.projectedCompletionDate = res.body.projectedCompletionDate != null ? moment(res.body.projectedCompletionDate) : null;
      res.body.actualCompletionDate = res.body.actualCompletionDate != null ? moment(res.body.actualCompletionDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((studyMilestone: IStudyMilestone) => {
        studyMilestone.projectedCompletionDate =
          studyMilestone.projectedCompletionDate != null ? moment(studyMilestone.projectedCompletionDate) : null;
        studyMilestone.actualCompletionDate =
          studyMilestone.actualCompletionDate != null ? moment(studyMilestone.actualCompletionDate) : null;
      });
    }
    return res;
  }
}
