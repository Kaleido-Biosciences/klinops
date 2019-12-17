import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IDataAnalysis } from 'app/shared/model/data-analysis.model';

type EntityResponseType = HttpResponse<IDataAnalysis>;
type EntityArrayResponseType = HttpResponse<IDataAnalysis[]>;

@Injectable({ providedIn: 'root' })
export class DataAnalysisService {
  public resourceUrl = SERVER_API_URL + 'api/data-analyses';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/data-analyses';

  constructor(protected http: HttpClient) {}

  create(dataAnalysis: IDataAnalysis): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dataAnalysis);
    return this.http
      .post<IDataAnalysis>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(dataAnalysis: IDataAnalysis): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dataAnalysis);
    return this.http
      .put<IDataAnalysis>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDataAnalysis>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDataAnalysis[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDataAnalysis[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(dataAnalysis: IDataAnalysis): IDataAnalysis {
    const copy: IDataAnalysis = Object.assign({}, dataAnalysis, {
      anticipatedAnalysisDeliveryDate:
        dataAnalysis.anticipatedAnalysisDeliveryDate != null && dataAnalysis.anticipatedAnalysisDeliveryDate.isValid()
          ? dataAnalysis.anticipatedAnalysisDeliveryDate.format(DATE_FORMAT)
          : null,
      actualAnalysisDeliveryDate:
        dataAnalysis.actualAnalysisDeliveryDate != null && dataAnalysis.actualAnalysisDeliveryDate.isValid()
          ? dataAnalysis.actualAnalysisDeliveryDate.format(DATE_FORMAT)
          : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.anticipatedAnalysisDeliveryDate =
        res.body.anticipatedAnalysisDeliveryDate != null ? moment(res.body.anticipatedAnalysisDeliveryDate) : null;
      res.body.actualAnalysisDeliveryDate =
        res.body.actualAnalysisDeliveryDate != null ? moment(res.body.actualAnalysisDeliveryDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((dataAnalysis: IDataAnalysis) => {
        dataAnalysis.anticipatedAnalysisDeliveryDate =
          dataAnalysis.anticipatedAnalysisDeliveryDate != null ? moment(dataAnalysis.anticipatedAnalysisDeliveryDate) : null;
        dataAnalysis.actualAnalysisDeliveryDate =
          dataAnalysis.actualAnalysisDeliveryDate != null ? moment(dataAnalysis.actualAnalysisDeliveryDate) : null;
      });
    }
    return res;
  }
}
