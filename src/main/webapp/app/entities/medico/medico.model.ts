import { IRelatorio } from 'app/entities/relatorio/relatorio.model';
import { IAdministrador } from 'app/entities/administrador/administrador.model';

export interface IMedico {
  id?: number;
  idMedico?: number | null;
  nome?: string | null;
  crm?: number | null;
  cpf?: number | null;
  rg?: number | null;
  especialidade?: string | null;
  contato?: number | null;
  relatorios?: IRelatorio[] | null;
  administrador?: IAdministrador | null;
}

export class Medico implements IMedico {
  constructor(
    public id?: number,
    public idMedico?: number | null,
    public nome?: string | null,
    public crm?: number | null,
    public cpf?: number | null,
    public rg?: number | null,
    public especialidade?: string | null,
    public contato?: number | null,
    public relatorios?: IRelatorio[] | null,
    public administrador?: IAdministrador | null
  ) {}
}

export function getMedicoIdentifier(medico: IMedico): number | undefined {
  return medico.id;
}
