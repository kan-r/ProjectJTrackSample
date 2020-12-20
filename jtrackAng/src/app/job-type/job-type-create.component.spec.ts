import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { JobTypeCreateComponent } from './job-type-create.component';

describe('JobTypeCreateComponent', () => {
  let component: JobTypeCreateComponent;
  let fixture: ComponentFixture<JobTypeCreateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ JobTypeCreateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(JobTypeCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
