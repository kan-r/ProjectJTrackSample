import { Component, OnInit } from '@angular/core';
import { JobResolution } from './job-resolution';
import { JobResolutionService } from './job-resolution.service';

@Component({
  selector: 'app-job-resolution',
  templateUrl: './job-resolution.component.html',
  // styleUrls: ['./job-resolution.component.css']
})
export class JobResolutionComponent implements OnInit {

  jobResolutionList: JobResolution[];

  constructor(
    private jobResolutionService: JobResolutionService) { }

  ngOnInit() {
    this.jobResolutionService.getJobResolutionList().subscribe(data => this.jobResolutionList = data);
  }

  deleteJobResolution(jobResolution: string): void {
    this.jobResolutionService.deleteJobResolution(jobResolution)
      .subscribe(
        _ => {this.jobResolutionService.getJobResolutionList().subscribe(data => this.jobResolutionList = data);}
      );
  }
}
