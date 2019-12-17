import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBioAnalysis } from 'app/shared/model/bio-analysis.model';

@Component({
  selector: 'ko-bio-analysis-detail',
  templateUrl: './bio-analysis-detail.component.html'
})
export class BioAnalysisDetailComponent implements OnInit {
  bioAnalysis: IBioAnalysis;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ bioAnalysis }) => {
      this.bioAnalysis = bioAnalysis;
    });
  }

  previousState() {
    window.history.back();
  }
}
