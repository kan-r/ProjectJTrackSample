import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { JobPriorityEditComponent } from './job-priority-edit.component';

describe('JobPriorityEditComponent', () => {
  let component: JobPriorityEditComponent;
  let fixture: ComponentFixture<JobPriorityEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ JobPriorityEditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(JobPriorityEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
