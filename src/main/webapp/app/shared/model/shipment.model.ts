/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { Moment } from 'moment';
import { IShipmentComponent } from 'app/shared/model/shipment-component.model';
import { ILaboratory } from 'app/shared/model/laboratory.model';
import { IClinicalStudy } from 'app/shared/model/clinical-study.model';

export interface IShipment {
  id?: number;
  shipmentCode?: string;
  dateShipped?: Moment;
  dateReceived?: Moment;
  components?: IShipmentComponent[];
  destination?: ILaboratory;
  study?: IClinicalStudy;
}

export class Shipment implements IShipment {
  constructor(
    public id?: number,
    public shipmentCode?: string,
    public dateShipped?: Moment,
    public dateReceived?: Moment,
    public components?: IShipmentComponent[],
    public destination?: ILaboratory,
    public study?: IClinicalStudy
  ) {}
}
