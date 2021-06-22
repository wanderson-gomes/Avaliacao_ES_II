import * as dayjs from 'dayjs';
import { IPlanoDeSaude } from 'app/entities/plano-de-saude/plano-de-saude.model';
import { IAdministrador } from 'app/entities/administrador/administrador.model';

export interface IPaciente {
  id?: number;
  idUsuario?: number | null;
  nome?: string | null;
  cpf?: number | null;
  rg?: number | null;
  dataNascimento?: dayjs.Dayjs | null;
  cidade?: string | null;
  bairro?: string | null;
  contato?: number | null;
  planoDeSaudes?: IPlanoDeSaude[] | null;
  administrador?: IAdministrador | null;
}

export class Paciente implements IPaciente {
  constructor(
    public id?: number,
    public idUsuario?: number | null,
    public nome?: string | null,
    public cpf?: number | null,
    public rg?: number | null,
    public dataNascimento?: dayjs.Dayjs | null,
    public cidade?: string | null,
    public bairro?: string | null,
    public contato?: number | null,
    public planoDeSaudes?: IPlanoDeSaude[] | null,
    public administrador?: IAdministrador | null
  ) {}
}

export function getPacienteIdentifier(paciente: IPaciente): number | undefined {
  return paciente.id;
}
