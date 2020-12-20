import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { JobPriority } from './job-priority';
import { JobPriorityService } from './job-priority.service';

@Component({
  selector: 'app-job-priority-create',
  templateUrl: './job-priority-create.component.html',
  // styleUrls: ['./job-priority-create.component.css']
})
export class JobPriorityCreateComponent implements OnInit {

  jobPriorityModel: JobPriority = new JobPriority();

  constructor(private jobPriorityService: JobPriorityService, private router: Router) { }

  ngOnInit() {
  }

  addJobPriority(): void{
    this.jobPriorityService.addJobPriority(this.jobPriorityModel)
      .subscribe(
        _ => {this.router.navigate(['/JobPriority']);}
      );
  }

}
