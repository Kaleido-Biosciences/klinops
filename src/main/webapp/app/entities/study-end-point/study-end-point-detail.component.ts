import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStudyEndPoint } from 'app/shared/model/study-end-point.model';

@Component({
  selector: 'ko-study-end-point-detail',
  templateUrl: './study-end-point-detail.component.html'
})
export class StudyEndPointDetailComponent implements OnInit {
  studyEndPoint: IStudyEndPoint;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ studyEndPoint }) => {
      this.studyEndPoint = studyEndPoint;
    });
  }

  previousState() {
    window.history.back();
  }
}
