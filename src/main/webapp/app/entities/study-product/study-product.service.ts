import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IStudyProduct } from 'app/shared/model/study-product.model';

type EntityResponseType = HttpResponse<IStudyProduct>;
type EntityArrayResponseType = HttpResponse<IStudyProduct[]>;

@Injectable({ providedIn: 'root' })
export class StudyProductService {
  public resourceUrl = SERVER_API_URL + 'api/study-products';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/study-products';

  constructor(protected http: HttpClient) {}

  create(studyProduct: IStudyProduct): Observable<EntityResponseType> {
    return this.http.post<IStudyProduct>(this.resourceUrl, studyProduct, { observe: 'response' });
  }

  update(studyProduct: IStudyProduct): Observable<EntityResponseType> {
    return this.http.put<IStudyProduct>(this.resourceUrl, studyProduct, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IStudyProduct>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStudyProduct[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStudyProduct[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
