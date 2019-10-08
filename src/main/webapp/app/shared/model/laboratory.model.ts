export interface ILaboratory {
  id?: number;
  labName?: string;
  streetAddress?: string;
  city?: string;
  state?: string;
  zip?: string;
  country?: string;
  labContactEmail?: string;
  labContactName?: string;
  labContactPhoneNumber?: string;
}

export class Laboratory implements ILaboratory {
  constructor(
    public id?: number,
    public labName?: string,
    public streetAddress?: string,
    public city?: string,
    public state?: string,
    public zip?: string,
    public country?: string,
    public labContactEmail?: string,
    public labContactName?: string,
    public labContactPhoneNumber?: string
  ) {}
}
