import { Component, OnInit } from '@angular/core';
import { JobType } from './job-type';
import { JobTypeService } from './job-type.service';

@Component({
  selector: 'app-job-type',
  templateUrl: './job-type.component.html',
  // styleUrls: ['./job-type.component.css']
})
export class JobTypeComponent implements OnInit {

  jobTypeList: JobType[];

  constructor(
    private jobTypeService: JobTypeService) { }

  ngOnInit() {
    this.jobTypeService.getJobTypeList().subscribe(data => this.jobTypeList = data);
  }

  deleteJobType(jobType: string): void {
    this.jobTypeService.deleteJobType(jobType)
      .subscribe(
        _ => {this.jobTypeService.getJobTypeList().subscribe(data => this.jobTypeList = data);}
      );
  }
}
