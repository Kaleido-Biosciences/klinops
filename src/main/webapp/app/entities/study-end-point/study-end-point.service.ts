import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IStudyEndPoint } from 'app/shared/model/study-end-point.model';

type EntityResponseType = HttpResponse<IStudyEndPoint>;
type EntityArrayResponseType = HttpResponse<IStudyEndPoint[]>;

@Injectable({ providedIn: 'root' })
export class StudyEndPointService {
  public resourceUrl = SERVER_API_URL + 'api/study-end-points';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/study-end-points';

  constructor(protected http: HttpClient) {}

  create(studyEndPoint: IStudyEndPoint): Observable<EntityResponseType> {
    return this.http.post<IStudyEndPoint>(this.resourceUrl, studyEndPoint, { observe: 'response' });
  }

  update(studyEndPoint: IStudyEndPoint): Observable<EntityResponseType> {
    return this.http.put<IStudyEndPoint>(this.resourceUrl, studyEndPoint, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IStudyEndPoint>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStudyEndPoint[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStudyEndPoint[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
