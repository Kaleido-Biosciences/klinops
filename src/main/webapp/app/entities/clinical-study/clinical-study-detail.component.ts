import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IClinicalStudy } from 'app/shared/model/clinical-study.model';

@Component({
  selector: 'ko-clinical-study-detail',
  templateUrl: './clinical-study-detail.component.html'
})
export class ClinicalStudyDetailComponent implements OnInit {
  clinicalStudy: IClinicalStudy;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ clinicalStudy }) => {
      this.clinicalStudy = clinicalStudy;
    });
  }

  previousState() {
    window.history.back();
  }
}
