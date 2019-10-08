import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStudyProduct } from 'app/shared/model/study-product.model';

@Component({
  selector: 'ko-study-product-detail',
  templateUrl: './study-product-detail.component.html'
})
export class StudyProductDetailComponent implements OnInit {
  studyProduct: IStudyProduct;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ studyProduct }) => {
      this.studyProduct = studyProduct;
    });
  }

  previousState() {
    window.history.back();
  }
}
