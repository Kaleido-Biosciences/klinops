import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'clinical-study',
        loadChildren: () => import('./clinical-study/clinical-study.module').then(m => m.KlinopsClinicalStudyModule)
      },
      {
        path: 'study-end-point',
        loadChildren: () => import('./study-end-point/study-end-point.module').then(m => m.KlinopsStudyEndPointModule)
      },
      {
        path: 'site',
        loadChildren: () => import('./site/site.module').then(m => m.KlinopsSiteModule)
      },
      {
        path: 'principal-investigator',
        loadChildren: () => import('./principal-investigator/principal-investigator.module').then(m => m.KlinopsPrincipalInvestigatorModule)
      },
      {
        path: 'study-milestone',
        loadChildren: () => import('./study-milestone/study-milestone.module').then(m => m.KlinopsStudyMilestoneModule)
      },
      {
        path: 'study-product',
        loadChildren: () => import('./study-product/study-product.module').then(m => m.KlinopsStudyProductModule)
      },
      {
        path: 'study-sample',
        loadChildren: () => import('./study-sample/study-sample.module').then(m => m.KlinopsStudySampleModule)
      },
      {
        path: 'shipment',
        loadChildren: () => import('./shipment/shipment.module').then(m => m.KlinopsShipmentModule)
      },
      {
        path: 'laboratory',
        loadChildren: () => import('./laboratory/laboratory.module').then(m => m.KlinopsLaboratoryModule)
      },
      {
        path: 'bio-analysis',
        loadChildren: () => import('./bio-analysis/bio-analysis.module').then(m => m.KlinopsBioAnalysisModule)
      },
      {
        path: 'data-analysis',
        loadChildren: () => import('./data-analysis/data-analysis.module').then(m => m.KlinopsDataAnalysisModule)
      },
      {
        path: 'trial-master-file',
        loadChildren: () => import('./trial-master-file/trial-master-file.module').then(m => m.KlinopsTrialMasterFileModule)
      },
      {
        path: 'shipment-component',
        loadChildren: () => import('./shipment-component/shipment-component.module').then(m => m.KlinopsShipmentComponentModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class KlinopsEntityModule {}
