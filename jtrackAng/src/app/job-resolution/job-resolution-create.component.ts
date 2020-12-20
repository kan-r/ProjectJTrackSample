import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { JobResolution } from './job-resolution';
import { JobResolutionService } from './job-resolution.service';

@Component({
  selector: 'app-job-resolution-create',
  templateUrl: './job-resolution-create.component.html',
  // styleUrls: ['./job-resolution-create.component.css']
})
export class JobResolutionCreateComponent implements OnInit {

  jobResolutionModel: JobResolution = new JobResolution();

  constructor(private jobResolutionService: JobResolutionService, private router: Router) { }

  ngOnInit() {
  }

  addJobResolution(): void{
    this.jobResolutionService.addJobResolution(this.jobResolutionModel)
      .subscribe(
        _ => {this.router.navigate(['/JobResolution']);}
      );
  }
}
