import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { JobStatusEditComponent } from './job-status-edit.component';

describe('JobStatusEditComponent', () => {
  let component: JobStatusEditComponent;
  let fixture: ComponentFixture<JobStatusEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ JobStatusEditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(JobStatusEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
