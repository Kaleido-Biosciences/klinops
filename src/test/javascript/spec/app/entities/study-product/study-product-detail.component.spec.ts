import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { KlinopsTestModule } from '../../../test.module';
import { StudyProductDetailComponent } from 'app/entities/study-product/study-product-detail.component';
import { StudyProduct } from 'app/shared/model/study-product.model';

describe('Component Tests', () => {
  describe('StudyProduct Management Detail Component', () => {
    let comp: StudyProductDetailComponent;
    let fixture: ComponentFixture<StudyProductDetailComponent>;
    const route = ({ data: of({ studyProduct: new StudyProduct(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KlinopsTestModule],
        declarations: [StudyProductDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(StudyProductDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(StudyProductDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.studyProduct).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
