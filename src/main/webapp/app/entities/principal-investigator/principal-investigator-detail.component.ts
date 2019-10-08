import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPrincipalInvestigator } from 'app/shared/model/principal-investigator.model';

@Component({
  selector: 'ko-principal-investigator-detail',
  templateUrl: './principal-investigator-detail.component.html'
})
export class PrincipalInvestigatorDetailComponent implements OnInit {
  principalInvestigator: IPrincipalInvestigator;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ principalInvestigator }) => {
      this.principalInvestigator = principalInvestigator;
    });
  }

  previousState() {
    window.history.back();
  }
}
