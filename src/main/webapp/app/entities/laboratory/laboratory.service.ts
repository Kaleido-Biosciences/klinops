/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ILaboratory } from 'app/shared/model/laboratory.model';

type EntityResponseType = HttpResponse<ILaboratory>;
type EntityArrayResponseType = HttpResponse<ILaboratory[]>;

@Injectable({ providedIn: 'root' })
export class LaboratoryService {
  public resourceUrl = SERVER_API_URL + 'api/laboratories';

  constructor(protected http: HttpClient) {}

  create(laboratory: ILaboratory): Observable<EntityResponseType> {
    return this.http.post<ILaboratory>(this.resourceUrl, laboratory, { observe: 'response' });
  }

  update(laboratory: ILaboratory): Observable<EntityResponseType> {
    return this.http.put<ILaboratory>(this.resourceUrl, laboratory, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILaboratory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILaboratory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
