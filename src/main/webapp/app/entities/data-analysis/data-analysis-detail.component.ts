/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDataAnalysis } from 'app/shared/model/data-analysis.model';

@Component({
  selector: 'ko-data-analysis-detail',
  templateUrl: './data-analysis-detail.component.html'
})
export class DataAnalysisDetailComponent implements OnInit {
  dataAnalysis: IDataAnalysis;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ dataAnalysis }) => {
      this.dataAnalysis = dataAnalysis;
    });
  }

  previousState() {
    window.history.back();
  }
}
