import * as dayjs from 'dayjs';
import { IPaciente } from 'app/entities/paciente/paciente.model';

export interface IConsulta {
  id?: number;
  idPaciente?: number | null;
  dataConsulta?: dayjs.Dayjs | null;
  horario?: string | null;
  paciente?: IPaciente | null;
}

export class Consulta implements IConsulta {
  constructor(
    public id?: number,
    public idPaciente?: number | null,
    public dataConsulta?: dayjs.Dayjs | null,
    public horario?: string | null,
    public paciente?: IPaciente | null
  ) {}
}

export function getConsultaIdentifier(consulta: IConsulta): number | undefined {
  return consulta.id;
}
