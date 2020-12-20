import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TimesheetCodeCreateComponent } from './timesheet-code-create.component';

describe('TimesheetCodeCreateComponent', () => {
  let component: TimesheetCodeCreateComponent;
  let fixture: ComponentFixture<TimesheetCodeCreateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TimesheetCodeCreateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TimesheetCodeCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
