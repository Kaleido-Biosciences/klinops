import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IStudySample } from 'app/shared/model/study-sample.model';

type EntityResponseType = HttpResponse<IStudySample>;
type EntityArrayResponseType = HttpResponse<IStudySample[]>;

@Injectable({ providedIn: 'root' })
export class StudySampleService {
  public resourceUrl = SERVER_API_URL + 'api/study-samples';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/study-samples';

  constructor(protected http: HttpClient) {}

  create(studySample: IStudySample): Observable<EntityResponseType> {
    return this.http.post<IStudySample>(this.resourceUrl, studySample, { observe: 'response' });
  }

  update(studySample: IStudySample): Observable<EntityResponseType> {
    return this.http.put<IStudySample>(this.resourceUrl, studySample, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IStudySample>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStudySample[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStudySample[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
