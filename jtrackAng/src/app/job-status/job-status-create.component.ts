import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { JobStatus } from './job-status';
import { JobStatusService } from './job-status.service';

@Component({
  selector: 'app-job-status-create',
  templateUrl: './job-status-create.component.html',
  // styleUrls: ['./job-status-create.component.css']
})
export class JobStatusCreateComponent implements OnInit {

  jobStatusModel: JobStatus = new JobStatus();

  constructor(private jobStatusService: JobStatusService, private router: Router) { }

  ngOnInit() {
  }

  addJobStatus(): void{
    this.jobStatusService.addJobStatus(this.jobStatusModel)
      .subscribe(
        _ => {this.router.navigate(['/JobStatus']);}
      );
  }

}
