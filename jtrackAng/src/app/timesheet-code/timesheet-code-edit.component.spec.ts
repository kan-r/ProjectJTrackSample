import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TimesheetCodeEditComponent } from './timesheet-code-edit.component';

describe('TimesheetCodeEditComponent', () => {
  let component: TimesheetCodeEditComponent;
  let fixture: ComponentFixture<TimesheetCodeEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TimesheetCodeEditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TimesheetCodeEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
