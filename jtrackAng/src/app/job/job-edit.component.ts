import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Job } from './job';
import { JobService } from './job.service';
import { JobType } from '../job-type/job-type';
import { JobTypeService } from '../job-type/job-type.service';
import { JobStage } from '../job-stage/job-stage';
import { JobStageService } from '../job-stage/job-stage.service';
import { JobPriority } from '../job-priority/job-priority';
import { JobPriorityService } from '../job-priority/job-priority.service';
import { JobStatus } from '../job-status/job-status';
import { JobStatusService } from '../job-status/job-status.service';
import { JobResolution } from '../job-resolution/job-resolution';
import { JobResolutionService } from '../job-resolution/job-resolution.service';
import { TimesheetCode } from '../timesheet-code/timesheet-code';
import { TimesheetCodeService } from '../timesheet-code/timesheet-code.service';
import { User } from '../user/user';
import { UserService } from '../user/user.service';

@Component({
  selector: 'app-job-edit',
  templateUrl: './job-edit.component.html',
  // styleUrls: ['./job-edit.component.css']
})
export class JobEditComponent implements OnInit {

  jobModel: Job = new Job();
  jobTypeList: JobType[];
  jobStageList: JobStage[];
  jobPriorityList: JobPriority[];
  jobStatusList: JobStatus[];
  jobResolutionList: JobResolution[];
  timesheetCodeList: TimesheetCode[];
  parentJobList: Job[];
  assignedToList: User[];

  constructor(
    private jobService: JobService,
    private jobTypeService: JobTypeService,
    private jobStageService: JobStageService,
    private jobPriorityService: JobPriorityService,
    private jobStatusService: JobStatusService,
    private jobResolutionService: JobResolutionService,
    private timesheetCodeService: TimesheetCodeService,
    private userService: UserService,
    private router: Router,
    private route: ActivatedRoute) { }

  ngOnInit() {
    this.getJob();
    this.jobTypeService.getJobTypeList().subscribe(data => this.jobTypeList = data);
    this.jobStageService.getJobStageList().subscribe(data => this.jobStageList = data);
    this.jobPriorityService.getJobPriorityList().subscribe(data => this.jobPriorityList = data);
    this.jobStatusService.getJobStatusList().subscribe(data => this.jobStatusList = data);
    this.jobResolutionService.getJobResolutionList().subscribe(data => this.jobResolutionList = data);
    this.timesheetCodeService.getTimesheetCodeList().subscribe(data => this.timesheetCodeList = data);
    this.jobService.getParentJobList().subscribe(data => this.parentJobList = data);
    this.userService.getUserList().subscribe(data => this.assignedToList = data);
  }

  getJob(): void {
    const jobNo: string = this.route.snapshot.paramMap.get('jobNo');
    this.jobService.getJob(jobNo)
      .subscribe(
        data => this.jobModel = data
      );
  }

  updateJob(): void{
    this.jobService.updateJob(this.jobModel)
      .subscribe(
        _ => this.router.navigate(['/Job'])
      );
  }
}
