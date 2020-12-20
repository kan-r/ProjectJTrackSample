import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { JobPriority } from './job-priority';
import { JobPriorityService } from './job-priority.service';

@Component({
  selector: 'app-job-priority-edit',
  templateUrl: './job-priority-edit.component.html',
  // styleUrls: ['./job-priority-edit.component.css']
})
export class JobPriorityEditComponent implements OnInit {

  jobPriorityModel: JobPriority = new JobPriority();

  constructor(private jobPriorityService: JobPriorityService, private router: Router, private route: ActivatedRoute) { }

  ngOnInit() {
    this.getJobPriority();
  }

  getJobPriority(): void {
    const userId: string = this.route.snapshot.paramMap.get('jobPriority');
    this.jobPriorityService.getJobPriority(userId)
      .subscribe(
        data => this.jobPriorityModel = data
      );
  }

  updateJobPriority(): void{
    this.jobPriorityService.updateJobPriority(this.jobPriorityModel)
      .subscribe(
        _ => {this.router.navigate(['/JobPriority']);}
      );
  }

}
