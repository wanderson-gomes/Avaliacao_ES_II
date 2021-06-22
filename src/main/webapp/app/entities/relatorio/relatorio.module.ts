import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { RelatorioComponent } from './list/relatorio.component';
import { RelatorioDetailComponent } from './detail/relatorio-detail.component';
import { RelatorioUpdateComponent } from './update/relatorio-update.component';
import { RelatorioDeleteDialogComponent } from './delete/relatorio-delete-dialog.component';
import { RelatorioRoutingModule } from './route/relatorio-routing.module';

@NgModule({
  imports: [SharedModule, RelatorioRoutingModule],
  declarations: [RelatorioComponent, RelatorioDetailComponent, RelatorioUpdateComponent, RelatorioDeleteDialogComponent],
  entryComponents: [RelatorioDeleteDialogComponent],
})
export class RelatorioModule {}
