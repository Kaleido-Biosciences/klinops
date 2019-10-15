/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { enableProdMode } from '@angular/core';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';

export function ProdConfig() {
  // disable debug data on prod profile to improve performance
  if (!DEBUG_INFO_ENABLED) {
    enableProdMode();
  }
}
