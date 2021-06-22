import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { RetornoComponent } from './list/retorno.component';
import { RetornoDetailComponent } from './detail/retorno-detail.component';
import { RetornoUpdateComponent } from './update/retorno-update.component';
import { RetornoDeleteDialogComponent } from './delete/retorno-delete-dialog.component';
import { RetornoRoutingModule } from './route/retorno-routing.module';

@NgModule({
  imports: [SharedModule, RetornoRoutingModule],
  declarations: [RetornoComponent, RetornoDetailComponent, RetornoUpdateComponent, RetornoDeleteDialogComponent],
  entryComponents: [RetornoDeleteDialogComponent],
})
export class RetornoModule {}
