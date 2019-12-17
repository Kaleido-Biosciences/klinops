import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ITrialMasterFile } from 'app/shared/model/trial-master-file.model';

type EntityResponseType = HttpResponse<ITrialMasterFile>;
type EntityArrayResponseType = HttpResponse<ITrialMasterFile[]>;

@Injectable({ providedIn: 'root' })
export class TrialMasterFileService {
  public resourceUrl = SERVER_API_URL + 'api/trial-master-files';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/trial-master-files';

  constructor(protected http: HttpClient) {}

  create(trialMasterFile: ITrialMasterFile): Observable<EntityResponseType> {
    return this.http.post<ITrialMasterFile>(this.resourceUrl, trialMasterFile, { observe: 'response' });
  }

  update(trialMasterFile: ITrialMasterFile): Observable<EntityResponseType> {
    return this.http.put<ITrialMasterFile>(this.resourceUrl, trialMasterFile, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITrialMasterFile>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITrialMasterFile[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITrialMasterFile[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
