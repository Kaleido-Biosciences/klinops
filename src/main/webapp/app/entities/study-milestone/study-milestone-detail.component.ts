import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStudyMilestone } from 'app/shared/model/study-milestone.model';

@Component({
  selector: 'ko-study-milestone-detail',
  templateUrl: './study-milestone-detail.component.html'
})
export class StudyMilestoneDetailComponent implements OnInit {
  studyMilestone: IStudyMilestone;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ studyMilestone }) => {
      this.studyMilestone = studyMilestone;
    });
  }

  previousState() {
    window.history.back();
  }
}
