import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { JobResolutionEditComponent } from './job-resolution-edit.component';

describe('JobResolutionEditComponent', () => {
  let component: JobResolutionEditComponent;
  let fixture: ComponentFixture<JobResolutionEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ JobResolutionEditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(JobResolutionEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
