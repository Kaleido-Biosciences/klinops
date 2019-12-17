import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStudySample } from 'app/shared/model/study-sample.model';

@Component({
  selector: 'ko-study-sample-detail',
  templateUrl: './study-sample-detail.component.html'
})
export class StudySampleDetailComponent implements OnInit {
  studySample: IStudySample;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ studySample }) => {
      this.studySample = studySample;
    });
  }

  previousState() {
    window.history.back();
  }
}
