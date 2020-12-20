import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { JobPriorityCreateComponent } from './job-priority-create.component';

describe('JobPriorityCreateComponent', () => {
  let component: JobPriorityCreateComponent;
  let fixture: ComponentFixture<JobPriorityCreateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ JobPriorityCreateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(JobPriorityCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
