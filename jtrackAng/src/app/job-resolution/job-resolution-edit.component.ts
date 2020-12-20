import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { JobResolution } from './job-resolution';
import { JobResolutionService } from './job-resolution.service';

@Component({
  selector: 'app-job-resolution-edit',
  templateUrl: './job-resolution-edit.component.html',
  // styleUrls: ['./job-resolution-edit.component.css']
})
export class JobResolutionEditComponent implements OnInit {

  jobResolutionModel: JobResolution = new JobResolution();

  constructor(private jobResolutionService: JobResolutionService, private router: Router, private route: ActivatedRoute) { }

  ngOnInit() {
    this.getJobResolution();
  }

  getJobResolution(): void {
    const userId: string = this.route.snapshot.paramMap.get('jobResolution');
    this.jobResolutionService.getJobResolution(userId)
      .subscribe(
        data => this.jobResolutionModel = data
      );
  }

  updateJobResolution(): void{
    this.jobResolutionService.updateJobResolution(this.jobResolutionModel)
      .subscribe(
        _ => {this.router.navigate(['/JobResolution']);}
      );
  }
}
