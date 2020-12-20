import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TimesheetCodeComponent } from './timesheet-code.component';

describe('TimesheetCodeComponent', () => {
  let component: TimesheetCodeComponent;
  let fixture: ComponentFixture<TimesheetCodeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TimesheetCodeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TimesheetCodeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
