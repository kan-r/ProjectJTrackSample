import { Component, OnInit } from '@angular/core';
import { JobPriority } from './job-priority';
import { JobPriorityService } from './job-priority.service';

@Component({
  selector: 'app-job-priority',
  templateUrl: './job-priority.component.html',
  // styleUrls: ['./job-priority.component.css']
})
export class JobPriorityComponent implements OnInit {

  jobPriorityList: JobPriority[];

  constructor(
    private jobPriorityService: JobPriorityService) { }

  ngOnInit() {
    this.jobPriorityService.getJobPriorityList().subscribe(data => this.jobPriorityList = data);
  }

  deleteJobPriority(jobPriority: string): void {
    this.jobPriorityService.deleteJobPriority(jobPriority)
      .subscribe(
        _ => {this.jobPriorityService.getJobPriorityList().subscribe(data => this.jobPriorityList = data);}
      );
  }

}
