import { TestBed } from '@angular/core/testing';

import { JobPriorityService } from './job-priority.service';

describe('JobPriorityService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: JobPriorityService = TestBed.get(JobPriorityService);
    expect(service).toBeTruthy();
  });
});
