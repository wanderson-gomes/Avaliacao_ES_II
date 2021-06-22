import { IMedico } from 'app/entities/medico/medico.model';

export interface IRelatorio {
  id?: number;
  idMedico?: number | null;
  descricao?: string | null;
  diagnostico?: string | null;
  medico?: IMedico | null;
}

export class Relatorio implements IRelatorio {
  constructor(
    public id?: number,
    public idMedico?: number | null,
    public descricao?: string | null,
    public diagnostico?: string | null,
    public medico?: IMedico | null
  ) {}
}

export function getRelatorioIdentifier(relatorio: IRelatorio): number | undefined {
  return relatorio.id;
}
