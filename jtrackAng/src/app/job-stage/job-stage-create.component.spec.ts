import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { JobStageCreateComponent } from './job-stage-create.component';

describe('JobStageCreateComponent', () => {
  let component: JobStageCreateComponent;
  let fixture: ComponentFixture<JobStageCreateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ JobStageCreateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(JobStageCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
