import { TestBed } from '@angular/core/testing';

import { JobStageService } from './job-stage.service';

describe('JobStageService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: JobStageService = TestBed.get(JobStageService);
    expect(service).toBeTruthy();
  });
});
