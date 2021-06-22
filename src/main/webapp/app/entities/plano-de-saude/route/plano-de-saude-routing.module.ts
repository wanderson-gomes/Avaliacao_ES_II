import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PlanoDeSaudeComponent } from '../list/plano-de-saude.component';
import { PlanoDeSaudeDetailComponent } from '../detail/plano-de-saude-detail.component';
import { PlanoDeSaudeUpdateComponent } from '../update/plano-de-saude-update.component';
import { PlanoDeSaudeRoutingResolveService } from './plano-de-saude-routing-resolve.service';

const planoDeSaudeRoute: Routes = [
  {
    path: '',
    component: PlanoDeSaudeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PlanoDeSaudeDetailComponent,
    resolve: {
      planoDeSaude: PlanoDeSaudeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PlanoDeSaudeUpdateComponent,
    resolve: {
      planoDeSaude: PlanoDeSaudeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PlanoDeSaudeUpdateComponent,
    resolve: {
      planoDeSaude: PlanoDeSaudeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(planoDeSaudeRoute)],
  exports: [RouterModule],
})
export class PlanoDeSaudeRoutingModule {}
