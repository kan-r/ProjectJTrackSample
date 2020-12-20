import { TestBed } from '@angular/core/testing';

import { JobTypeService } from './job-type.service';

describe('JobTypeService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: JobTypeService = TestBed.get(JobTypeService);
    expect(service).toBeTruthy();
  });
});
