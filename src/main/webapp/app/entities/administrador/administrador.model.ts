import * as dayjs from 'dayjs';
import { IPaciente } from 'app/entities/paciente/paciente.model';
import { IMedico } from 'app/entities/medico/medico.model';

export interface IAdministrador {
  id?: number;
  idAdministrador?: number | null;
  nome?: string | null;
  cpf?: number | null;
  rg?: number | null;
  dataNascimento?: dayjs.Dayjs | null;
  cidade?: string | null;
  bairro?: string | null;
  contato?: number | null;
  pacientes?: IPaciente[] | null;
  medicos?: IMedico[] | null;
}

export class Administrador implements IAdministrador {
  constructor(
    public id?: number,
    public idAdministrador?: number | null,
    public nome?: string | null,
    public cpf?: number | null,
    public rg?: number | null,
    public dataNascimento?: dayjs.Dayjs | null,
    public cidade?: string | null,
    public bairro?: string | null,
    public contato?: number | null,
    public pacientes?: IPaciente[] | null,
    public medicos?: IMedico[] | null
  ) {}
}

export function getAdministradorIdentifier(administrador: IAdministrador): number | undefined {
  return administrador.id;
}
