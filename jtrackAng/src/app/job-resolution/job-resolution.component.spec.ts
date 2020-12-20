import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { JobResolutionComponent } from './job-resolution.component';

describe('JobResolutionComponent', () => {
  let component: JobResolutionComponent;
  let fixture: ComponentFixture<JobResolutionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ JobResolutionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(JobResolutionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
