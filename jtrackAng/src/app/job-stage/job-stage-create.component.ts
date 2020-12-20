import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { JobStage } from './job-stage';
import { JobStageService } from './job-stage.service';

@Component({
  selector: 'app-job-stage-create',
  templateUrl: './job-stage-create.component.html',
  // styleUrls: ['./job-stage-create.component.css']
})
export class JobStageCreateComponent implements OnInit {

  jobStageModel: JobStage = new JobStage();

  constructor(private jobStageService: JobStageService, private router: Router) { }

  ngOnInit() {
  }

  addJobStage(): void{
    this.jobStageService.addJobStage(this.jobStageModel)
      .subscribe(
        _ => {this.router.navigate(['/JobStage']);}
      );
  }
}
