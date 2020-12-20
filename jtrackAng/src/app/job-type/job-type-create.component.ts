import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { JobType } from './job-type';
import { JobTypeService } from './job-type.service';

@Component({
  selector: 'app-job-type-create',
  templateUrl: './job-type-create.component.html',
  // styleUrls: ['./job-type-create.component.css']
})
export class JobTypeCreateComponent implements OnInit {

  jobTypeModel: JobType = new JobType();

  constructor(private jobTypeService: JobTypeService, private router: Router) { }

  ngOnInit() {
  }

  addJobType(): void{
    this.jobTypeService.addJobType(this.jobTypeModel)
      .subscribe(
        _ => {this.router.navigate(['/JobType']);}
      );
  }
}
