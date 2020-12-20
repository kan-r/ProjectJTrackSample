import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { JobStageEditComponent } from './job-stage-edit.component';

describe('JobStageEditComponent', () => {
  let component: JobStageEditComponent;
  let fixture: ComponentFixture<JobStageEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ JobStageEditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(JobStageEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
