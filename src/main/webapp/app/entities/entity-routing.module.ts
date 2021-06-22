import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'paciente',
        data: { pageTitle: 'consultaOnlineApp.paciente.home.title' },
        loadChildren: () => import('./paciente/paciente.module').then(m => m.PacienteModule),
      },
      {
        path: 'administrador',
        data: { pageTitle: 'consultaOnlineApp.administrador.home.title' },
        loadChildren: () => import('./administrador/administrador.module').then(m => m.AdministradorModule),
      },
      {
        path: 'plano-de-saude',
        data: { pageTitle: 'consultaOnlineApp.planoDeSaude.home.title' },
        loadChildren: () => import('./plano-de-saude/plano-de-saude.module').then(m => m.PlanoDeSaudeModule),
      },
      {
        path: 'consulta',
        data: { pageTitle: 'consultaOnlineApp.consulta.home.title' },
        loadChildren: () => import('./consulta/consulta.module').then(m => m.ConsultaModule),
      },
      {
        path: 'medico',
        data: { pageTitle: 'consultaOnlineApp.medico.home.title' },
        loadChildren: () => import('./medico/medico.module').then(m => m.MedicoModule),
      },
      {
        path: 'relatorio',
        data: { pageTitle: 'consultaOnlineApp.relatorio.home.title' },
        loadChildren: () => import('./relatorio/relatorio.module').then(m => m.RelatorioModule),
      },
      {
        path: 'retorno',
        data: { pageTitle: 'consultaOnlineApp.retorno.home.title' },
        loadChildren: () => import('./retorno/retorno.module').then(m => m.RetornoModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
