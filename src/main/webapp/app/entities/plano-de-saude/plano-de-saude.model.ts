import { IPaciente } from 'app/entities/paciente/paciente.model';
import { FormaDePag } from 'app/entities/enumerations/forma-de-pag.model';

export interface IPlanoDeSaude {
  id?: number;
  idPlano?: number | null;
  nomePlano?: string | null;
  ativo?: boolean | null;
  cnpj?: number | null;
  formaDePag?: FormaDePag | null;
  paciente?: IPaciente | null;
}

export class PlanoDeSaude implements IPlanoDeSaude {
  constructor(
    public id?: number,
    public idPlano?: number | null,
    public nomePlano?: string | null,
    public ativo?: boolean | null,
    public cnpj?: number | null,
    public formaDePag?: FormaDePag | null,
    public paciente?: IPaciente | null
  ) {
    this.ativo = this.ativo ?? false;
  }
}

export function getPlanoDeSaudeIdentifier(planoDeSaude: IPlanoDeSaude): number | undefined {
  return planoDeSaude.id;
}
