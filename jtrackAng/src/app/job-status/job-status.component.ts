import { Component, OnInit } from '@angular/core';
import { JobStatus } from './job-status';
import { JobStatusService } from './job-status.service';

@Component({
  selector: 'app-job-status',
  templateUrl: './job-status.component.html',
  // styleUrls: ['./job-status.component.css']
})
export class JobStatusComponent implements OnInit {

  jobStatusList: JobStatus[];

  constructor(
    private jobStatusService: JobStatusService) { }

  ngOnInit() {
    this.jobStatusService.getJobStatusList().subscribe(data => this.jobStatusList = data);
  }

  deleteJobStatus(jobType: string): void {
    this.jobStatusService.deleteJobStatus(jobType)
      .subscribe(
        _ => {this.jobStatusService.getJobStatusList().subscribe(data => this.jobStatusList = data);}
      );
  }

}
