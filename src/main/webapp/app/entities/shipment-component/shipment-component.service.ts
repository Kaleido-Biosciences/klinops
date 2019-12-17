import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IShipmentComponent } from 'app/shared/model/shipment-component.model';

type EntityResponseType = HttpResponse<IShipmentComponent>;
type EntityArrayResponseType = HttpResponse<IShipmentComponent[]>;

@Injectable({ providedIn: 'root' })
export class ShipmentComponentService {
  public resourceUrl = SERVER_API_URL + 'api/shipment-components';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/shipment-components';

  constructor(protected http: HttpClient) {}

  create(shipmentComponent: IShipmentComponent): Observable<EntityResponseType> {
    return this.http.post<IShipmentComponent>(this.resourceUrl, shipmentComponent, { observe: 'response' });
  }

  update(shipmentComponent: IShipmentComponent): Observable<EntityResponseType> {
    return this.http.put<IShipmentComponent>(this.resourceUrl, shipmentComponent, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IShipmentComponent>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IShipmentComponent[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IShipmentComponent[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
