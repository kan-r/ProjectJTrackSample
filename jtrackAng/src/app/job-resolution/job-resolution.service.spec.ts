import { TestBed } from '@angular/core/testing';

import { JobResolutionService } from './job-resolution.service';

describe('JobResolutionService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: JobResolutionService = TestBed.get(JobResolutionService);
    expect(service).toBeTruthy();
  });
});
