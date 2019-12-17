import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { of } from 'rxjs';

import { KlinopsTestModule } from '../../../test.module';
import { KoMetricsMonitoringComponent } from 'app/admin/metrics/metrics.component';
import { KoMetricsService } from 'app/admin/metrics/metrics.service';

describe('Component Tests', () => {
  describe('KoMetricsMonitoringComponent', () => {
    let comp: KoMetricsMonitoringComponent;
    let fixture: ComponentFixture<KoMetricsMonitoringComponent>;
    let service: KoMetricsService;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [KlinopsTestModule],
        declarations: [KoMetricsMonitoringComponent]
      })
        .overrideTemplate(KoMetricsMonitoringComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(KoMetricsMonitoringComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(KoMetricsService);
    });

    describe('refresh', () => {
      it('should call refresh on init', () => {
        // GIVEN
        const response = {
          timers: {
            service: 'test',
            unrelatedKey: 'test'
          },
          gauges: {
            'jcache.statistics': {
              value: 2
            },
            unrelatedKey: 'test'
          }
        };
        spyOn(service, 'getMetrics').and.returnValue(of(response));

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(service.getMetrics).toHaveBeenCalled();
      });
    });
  });
});
