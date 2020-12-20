import { Component, OnInit } from '@angular/core';
import { Timesheet, TimesheetSO } from './timesheet';
import { TimesheetService } from './timesheet.service';
import { User } from '../user/user';
import { UserService } from '../user/user.service';

@Component({
  selector: 'app-timesheet',
  templateUrl: './timesheet.component.html',
  // styleUrls: ['./timesheet.component.css']
})
export class TimesheetComponent implements OnInit {

  timesheetList: Timesheet[];
  userList: User[];
  timesheetSOModel: TimesheetSO = new TimesheetSO();

  constructor(
    private timesheetService: TimesheetService, 
    private userService: UserService) { }

  ngOnInit() {
    this.userService.getUserList().subscribe(data => this.userList = data);
    this.timesheetService.getTimesheetList().subscribe(data => this.timesheetList = data);
  }

  getTimeSheetList(): void {
    this.timesheetService.getTimesheetList2(this.timesheetSOModel)
      .subscribe(
        data => this.timesheetList = data
      );
  }

  deleteTimesheet(timesheetId: string): void {
    this.timesheetService.deleteTimesheet(timesheetId)
      .subscribe(
        _ => {this.timesheetService.getTimesheetList().subscribe(data => this.timesheetList = data);}
      );
  }
}
