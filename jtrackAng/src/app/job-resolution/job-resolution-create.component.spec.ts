import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { JobResolutionCreateComponent } from './job-resolution-create.component';

describe('JobResolutionCreateComponent', () => {
  let component: JobResolutionCreateComponent;
  let fixture: ComponentFixture<JobResolutionCreateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ JobResolutionCreateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(JobResolutionCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
