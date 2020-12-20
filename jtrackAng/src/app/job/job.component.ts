import { Component, OnInit } from '@angular/core';
import { Job, JobSO } from './job';
import { JobService } from './job.service';
import { JobType } from '../job-type/job-type';
import { JobTypeService } from '../job-type/job-type.service';
import { JobStatus } from '../job-status/job-status';
import { JobStatusService } from '../job-status/job-status.service';
import { User } from '../user/user';
import { UserService } from '../user/user.service';
import { MessageService } from '../message/message.service';

@Component({
  selector: 'app-job',
  templateUrl: './job.component.html',
  // styleUrls: ['./job.component.css']
})
export class JobComponent implements OnInit {

  jobList: Job[];
  jobTypeList: JobType[];
  jobStatusList: JobStatus[];
  assignedToList: User[];
  jobSOModel: JobSO = new JobSO();

  constructor(
    private jobService: JobService,
    private jobTypeService: JobTypeService,
    private jobStatusService: JobStatusService,
    private userService: UserService,
    private messageService: MessageService) { }

  ngOnInit() {
    this.messageService.clearMessage();
    this.getJobList();
    this.jobTypeService.getJobTypeList().subscribe(data => this.jobTypeList = data);
    this.jobStatusService.getJobStatusList().subscribe(data => this.jobStatusList = data);
    this.userService.getUserList().subscribe(data => this.assignedToList = data);
  }

  getJobList(): void {
    this.jobService.getJobList2(this.jobSOModel)
      .subscribe(
        data => this.jobList = data
      );
  }

  deleteJob(jobNo: string): void {
    this.jobService.deleteJob(jobNo)
      .subscribe(
        _ => {this.getJobList()}
      );
  }
}
