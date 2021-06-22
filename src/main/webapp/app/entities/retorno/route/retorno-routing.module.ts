import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RetornoComponent } from '../list/retorno.component';
import { RetornoDetailComponent } from '../detail/retorno-detail.component';
import { RetornoUpdateComponent } from '../update/retorno-update.component';
import { RetornoRoutingResolveService } from './retorno-routing-resolve.service';

const retornoRoute: Routes = [
  {
    path: '',
    component: RetornoComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RetornoDetailComponent,
    resolve: {
      retorno: RetornoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RetornoUpdateComponent,
    resolve: {
      retorno: RetornoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RetornoUpdateComponent,
    resolve: {
      retorno: RetornoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(retornoRoute)],
  exports: [RouterModule],
})
export class RetornoRoutingModule {}
