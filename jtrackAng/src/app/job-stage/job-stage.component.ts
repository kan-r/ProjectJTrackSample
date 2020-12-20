import { Component, OnInit } from '@angular/core';
import { JobStage } from './job-stage';
import { JobStageService } from './job-stage.service';

@Component({
  selector: 'app-job-stage',
  templateUrl: './job-stage.component.html',
  // styleUrls: ['./job-stage.component.css']
})
export class JobStageComponent implements OnInit {

  jobStageList: JobStage[];

  constructor(
    private jobStageService: JobStageService) { }

  ngOnInit() {
    this.jobStageService.getJobStageList().subscribe(data => this.jobStageList = data);
  }

  deleteJobStage(jobStage: string): void {
    this.jobStageService.deleteJobStage(jobStage)
      .subscribe(
        _ => {this.jobStageService.getJobStageList().subscribe(data => this.jobStageList = data);}
      );
  }
}
