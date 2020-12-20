import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { JobStatusCreateComponent } from './job-status-create.component';

describe('JobStatusCreateComponent', () => {
  let component: JobStatusCreateComponent;
  let fixture: ComponentFixture<JobStatusCreateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ JobStatusCreateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(JobStatusCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
