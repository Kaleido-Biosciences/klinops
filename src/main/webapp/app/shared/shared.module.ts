/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { NgModule } from '@angular/core';
import { KlinopsSharedLibsModule } from './shared-libs.module';
import { FindLanguageFromKeyPipe } from './language/find-language-from-key.pipe';
import { KoAlertComponent } from './alert/alert.component';
import { KoAlertErrorComponent } from './alert/alert-error.component';
import { HasAnyAuthorityDirective } from './auth/has-any-authority.directive';

@NgModule({
  imports: [KlinopsSharedLibsModule],
  declarations: [FindLanguageFromKeyPipe, KoAlertComponent, KoAlertErrorComponent, HasAnyAuthorityDirective],
  exports: [KlinopsSharedLibsModule, FindLanguageFromKeyPipe, KoAlertComponent, KoAlertErrorComponent, HasAnyAuthorityDirective]
})
export class KlinopsSharedModule {}
