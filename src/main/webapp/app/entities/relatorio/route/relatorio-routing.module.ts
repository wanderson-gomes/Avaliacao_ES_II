import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RelatorioComponent } from '../list/relatorio.component';
import { RelatorioDetailComponent } from '../detail/relatorio-detail.component';
import { RelatorioUpdateComponent } from '../update/relatorio-update.component';
import { RelatorioRoutingResolveService } from './relatorio-routing-resolve.service';

const relatorioRoute: Routes = [
  {
    path: '',
    component: RelatorioComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RelatorioDetailComponent,
    resolve: {
      relatorio: RelatorioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RelatorioUpdateComponent,
    resolve: {
      relatorio: RelatorioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RelatorioUpdateComponent,
    resolve: {
      relatorio: RelatorioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(relatorioRoute)],
  exports: [RouterModule],
})
export class RelatorioRoutingModule {}
