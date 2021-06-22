import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { PlanoDeSaudeComponent } from './list/plano-de-saude.component';
import { PlanoDeSaudeDetailComponent } from './detail/plano-de-saude-detail.component';
import { PlanoDeSaudeUpdateComponent } from './update/plano-de-saude-update.component';
import { PlanoDeSaudeDeleteDialogComponent } from './delete/plano-de-saude-delete-dialog.component';
import { PlanoDeSaudeRoutingModule } from './route/plano-de-saude-routing.module';

@NgModule({
  imports: [SharedModule, PlanoDeSaudeRoutingModule],
  declarations: [PlanoDeSaudeComponent, PlanoDeSaudeDetailComponent, PlanoDeSaudeUpdateComponent, PlanoDeSaudeDeleteDialogComponent],
  entryComponents: [PlanoDeSaudeDeleteDialogComponent],
})
export class PlanoDeSaudeModule {}
