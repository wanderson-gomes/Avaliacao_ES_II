import * as dayjs from 'dayjs';
import { IMedico } from 'app/entities/medico/medico.model';

export interface IRetorno {
  id?: number;
  idMedico?: number | null;
  idPaciente?: number | null;
  dataRetorno?: dayjs.Dayjs | null;
  medico?: IMedico | null;
}

export class Retorno implements IRetorno {
  constructor(
    public id?: number,
    public idMedico?: number | null,
    public idPaciente?: number | null,
    public dataRetorno?: dayjs.Dayjs | null,
    public medico?: IMedico | null
  ) {}
}

export function getRetornoIdentifier(retorno: IRetorno): number | undefined {
  return retorno.id;
}
