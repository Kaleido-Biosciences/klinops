/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { KoMetricsService } from './metrics.service';

@Component({
  selector: 'ko-metrics',
  templateUrl: './metrics.component.html'
})
export class KoMetricsMonitoringComponent implements OnInit {
  metrics: any = {};
  threadData: any = {};
  updatingMetrics = true;
  JCACHE_KEY: string;

  constructor(private modalService: NgbModal, private metricsService: KoMetricsService) {
    this.JCACHE_KEY = 'jcache.statistics';
  }

  ngOnInit() {
    this.refresh();
  }

  refresh() {
    this.updatingMetrics = true;
    this.metricsService.getMetrics().subscribe(metrics => {
      this.metrics = metrics;
      this.metricsService.threadDump().subscribe(data => {
        this.threadData = data.threads;
        this.updatingMetrics = false;
      });
    });
  }

  isObjectExisting(metrics: any, key: string) {
    return metrics && metrics[key];
  }

  isObjectExistingAndNotEmpty(metrics: any, key: string) {
    return this.isObjectExisting(metrics, key) && JSON.stringify(metrics[key]) !== '{}';
  }
}
