import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { JobStage } from './job-stage';
import { JobStageService } from './job-stage.service';

@Component({
  selector: 'app-job-stage-edit',
  templateUrl: './job-stage-edit.component.html',
  // styleUrls: ['./job-stage-edit.component.css']
})
export class JobStageEditComponent implements OnInit {

  jobStageModel: JobStage = new JobStage();

  constructor(private jobStageService: JobStageService, private router: Router, private route: ActivatedRoute) { }

  ngOnInit() {
    this.getJobStage();
  }

  getJobStage(): void {
    const userId: string = this.route.snapshot.paramMap.get('jobStage');
    this.jobStageService.getJobStage(userId)
      .subscribe(
        data => this.jobStageModel = data
      );
  }

  updateJobStage(): void{
    this.jobStageService.updateJobStage(this.jobStageModel)
      .subscribe(
        _ => {this.router.navigate(['/JobStage']);}
      );
  }
}
