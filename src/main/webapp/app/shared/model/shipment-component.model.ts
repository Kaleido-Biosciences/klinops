import { IShipment } from 'app/shared/model/shipment.model';

export interface IShipmentComponent {
  id?: number;
  sampleType?: string;
  sampleCount?: number;
  shipment?: IShipment;
}

export class ShipmentComponent implements IShipmentComponent {
  constructor(public id?: number, public sampleType?: string, public sampleCount?: number, public shipment?: IShipment) {}
}
