import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { JobTypeEditComponent } from './job-type-edit.component';

describe('JobTypeEditComponent', () => {
  let component: JobTypeEditComponent;
  let fixture: ComponentFixture<JobTypeEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ JobTypeEditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(JobTypeEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
