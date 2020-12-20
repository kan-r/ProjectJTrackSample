import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { JobType } from './job-type';
import { JobTypeService } from './job-type.service';

@Component({
  selector: 'app-job-type-edit',
  templateUrl: './job-type-edit.component.html',
  // styleUrls: ['./job-type-edit.component.css']
})
export class JobTypeEditComponent implements OnInit {

  jobTypeModel: JobType = new JobType();

  constructor(private jobTypeService: JobTypeService, private router: Router, private route: ActivatedRoute) { }

  ngOnInit() {
    this.getJobType();
  }

  getJobType(): void {
    const userId: string = this.route.snapshot.paramMap.get('jobType');
    this.jobTypeService.getJobType(userId)
      .subscribe(
        data => this.jobTypeModel = data
      );
  }

  updateJobType(): void{
    this.jobTypeService.updateJobType(this.jobTypeModel)
      .subscribe(
        _ => {this.router.navigate(['/JobType']);}
      );
  }
}
