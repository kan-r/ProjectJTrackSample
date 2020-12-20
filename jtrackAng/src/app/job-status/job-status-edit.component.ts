import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { JobStatus } from './job-status';
import { JobStatusService } from './job-status.service';

@Component({
  selector: 'app-job-status-edit',
  templateUrl: './job-status-edit.component.html',
  // styleUrls: ['./job-status-edit.component.css']
})
export class JobStatusEditComponent implements OnInit {

  jobStatusModel: JobStatus = new JobStatus();

  constructor(private jobStatusService: JobStatusService, private router: Router, private route: ActivatedRoute) { }

  ngOnInit() {
    this.getJobStatus();
  }

  getJobStatus(): void {
    const userId: string = this.route.snapshot.paramMap.get('jobStatus');
    this.jobStatusService.getJobStatus(userId)
      .subscribe(
        data => this.jobStatusModel = data
      );
  }

  updateJobStatus(): void{
    this.jobStatusService.updateJobStatus(this.jobStatusModel)
      .subscribe(
        _ => {this.router.navigate(['/JobStatus']);}
      );
  }

}
