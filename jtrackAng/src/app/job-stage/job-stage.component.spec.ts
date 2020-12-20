import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { JobStageComponent } from './job-stage.component';

describe('JobStageComponent', () => {
  let component: JobStageComponent;
  let fixture: ComponentFixture<JobStageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ JobStageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(JobStageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
