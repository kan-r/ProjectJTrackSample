import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Timesheet } from './timesheet';
import { TimesheetService } from './timesheet.service';
import { User } from '../user/user';
import { UserService } from '../user/user.service';
import { Job } from '../job/job';
import { JobService } from '../job/job.service';
import { TimesheetCode } from '../timesheet-code/timesheet-code';
import { TimesheetCodeService } from '../timesheet-code/timesheet-code.service';

@Component({
  selector: 'app-timesheet-create',
  templateUrl: './timesheet-create.component.html',
  // styleUrls: ['./timesheet-create.component.css']
})
export class TimesheetCreateComponent implements OnInit {

  timesheetModel: Timesheet = new Timesheet();
  userList: User[];
  jobList: Job[];
  timesheetCodeList: TimesheetCode[];

  constructor(
    private timesheetService: TimesheetService, 
    private userService: UserService,
    private jobService: JobService,
    private timesheetCodeService: TimesheetCodeService,
    private router: Router) { }

  ngOnInit() {
    this.userService.getUserList().subscribe(data => this.userList = data);
    this.jobService.getJobList().subscribe(data => this.jobList = data);
    this.timesheetCodeService.getTimesheetCodeList().subscribe(data => this.timesheetCodeList = data);
  }

  addTimesheet(): void{
    this.timesheetService.addTimesheet(this.timesheetModel)
      .subscribe(
        _ => {this.router.navigate(['/Timesheet']);}
      );
  }
}
