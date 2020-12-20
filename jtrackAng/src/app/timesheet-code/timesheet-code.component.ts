import { Component, OnInit } from '@angular/core';
import { TimesheetCode } from './timesheet-code';
import { TimesheetCodeService } from './timesheet-code.service';

@Component({
  selector: 'app-timesheet-code',
  templateUrl: './timesheet-code.component.html',
  // styleUrls: ['./timesheet-code.component.css']
})
export class TimesheetCodeComponent implements OnInit {

  timesheetCodeList: TimesheetCode[];

  constructor(
    private timesheetCodeService: TimesheetCodeService) { }

  ngOnInit() {
    this.timesheetCodeService.getTimesheetCodeList().subscribe(data => this.timesheetCodeList = data);
  }

  deleteTimesheetCode(timesheetCode: string): void {
    this.timesheetCodeService.deleteTimesheetCode(timesheetCode)
      .subscribe(
        _ => {this.timesheetCodeService.getTimesheetCodeList().subscribe(data => this.timesheetCodeList = data);}
      );
  }
}
