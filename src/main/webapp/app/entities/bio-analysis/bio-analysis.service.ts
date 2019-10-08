import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IBioAnalysis } from 'app/shared/model/bio-analysis.model';

type EntityResponseType = HttpResponse<IBioAnalysis>;
type EntityArrayResponseType = HttpResponse<IBioAnalysis[]>;

@Injectable({ providedIn: 'root' })
export class BioAnalysisService {
  public resourceUrl = SERVER_API_URL + 'api/bio-analyses';

  constructor(protected http: HttpClient) {}

  create(bioAnalysis: IBioAnalysis): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bioAnalysis);
    return this.http
      .post<IBioAnalysis>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(bioAnalysis: IBioAnalysis): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bioAnalysis);
    return this.http
      .put<IBioAnalysis>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IBioAnalysis>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBioAnalysis[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(bioAnalysis: IBioAnalysis): IBioAnalysis {
    const copy: IBioAnalysis = Object.assign({}, bioAnalysis, {
      anticipatedLabWorkStartDate:
        bioAnalysis.anticipatedLabWorkStartDate != null && bioAnalysis.anticipatedLabWorkStartDate.isValid()
          ? bioAnalysis.anticipatedLabWorkStartDate.format(DATE_FORMAT)
          : null,
      actualLabWorkStartDate:
        bioAnalysis.actualLabWorkStartDate != null && bioAnalysis.actualLabWorkStartDate.isValid()
          ? bioAnalysis.actualLabWorkStartDate.format(DATE_FORMAT)
          : null,
      anticipatedLabResultDeliveryDate:
        bioAnalysis.anticipatedLabResultDeliveryDate != null && bioAnalysis.anticipatedLabResultDeliveryDate.isValid()
          ? bioAnalysis.anticipatedLabResultDeliveryDate.format(DATE_FORMAT)
          : null,
      actualLabResultDeliveryDate:
        bioAnalysis.actualLabResultDeliveryDate != null && bioAnalysis.actualLabResultDeliveryDate.isValid()
          ? bioAnalysis.actualLabResultDeliveryDate.format(DATE_FORMAT)
          : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.anticipatedLabWorkStartDate =
        res.body.anticipatedLabWorkStartDate != null ? moment(res.body.anticipatedLabWorkStartDate) : null;
      res.body.actualLabWorkStartDate = res.body.actualLabWorkStartDate != null ? moment(res.body.actualLabWorkStartDate) : null;
      res.body.anticipatedLabResultDeliveryDate =
        res.body.anticipatedLabResultDeliveryDate != null ? moment(res.body.anticipatedLabResultDeliveryDate) : null;
      res.body.actualLabResultDeliveryDate =
        res.body.actualLabResultDeliveryDate != null ? moment(res.body.actualLabResultDeliveryDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((bioAnalysis: IBioAnalysis) => {
        bioAnalysis.anticipatedLabWorkStartDate =
          bioAnalysis.anticipatedLabWorkStartDate != null ? moment(bioAnalysis.anticipatedLabWorkStartDate) : null;
        bioAnalysis.actualLabWorkStartDate = bioAnalysis.actualLabWorkStartDate != null ? moment(bioAnalysis.actualLabWorkStartDate) : null;
        bioAnalysis.anticipatedLabResultDeliveryDate =
          bioAnalysis.anticipatedLabResultDeliveryDate != null ? moment(bioAnalysis.anticipatedLabResultDeliveryDate) : null;
        bioAnalysis.actualLabResultDeliveryDate =
          bioAnalysis.actualLabResultDeliveryDate != null ? moment(bioAnalysis.actualLabResultDeliveryDate) : null;
      });
    }
    return res;
  }
}
