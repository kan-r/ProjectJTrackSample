import { TestBed } from '@angular/core/testing';

import { TimesheetCodeService } from './timesheet-code.service';

describe('TimesheetCodeService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: TimesheetCodeService = TestBed.get(TimesheetCodeService);
    expect(service).toBeTruthy();
  });
});
