/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPrincipalInvestigator } from 'app/shared/model/principal-investigator.model';

type EntityResponseType = HttpResponse<IPrincipalInvestigator>;
type EntityArrayResponseType = HttpResponse<IPrincipalInvestigator[]>;

@Injectable({ providedIn: 'root' })
export class PrincipalInvestigatorService {
  public resourceUrl = SERVER_API_URL + 'api/principal-investigators';

  constructor(protected http: HttpClient) {}

  create(principalInvestigator: IPrincipalInvestigator): Observable<EntityResponseType> {
    return this.http.post<IPrincipalInvestigator>(this.resourceUrl, principalInvestigator, { observe: 'response' });
  }

  update(principalInvestigator: IPrincipalInvestigator): Observable<EntityResponseType> {
    return this.http.put<IPrincipalInvestigator>(this.resourceUrl, principalInvestigator, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPrincipalInvestigator>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPrincipalInvestigator[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
