/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITrialMasterFile } from 'app/shared/model/trial-master-file.model';

@Component({
  selector: 'ko-trial-master-file-detail',
  templateUrl: './trial-master-file-detail.component.html'
})
export class TrialMasterFileDetailComponent implements OnInit {
  trialMasterFile: ITrialMasterFile;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ trialMasterFile }) => {
      this.trialMasterFile = trialMasterFile;
    });
  }

  previousState() {
    window.history.back();
  }
}
