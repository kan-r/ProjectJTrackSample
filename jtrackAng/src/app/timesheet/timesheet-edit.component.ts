import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Timesheet } from './timesheet';
import { TimesheetService } from './timesheet.service';
import { TimesheetCode } from '../timesheet-code/timesheet-code';
import { TimesheetCodeService } from '../timesheet-code/timesheet-code.service';

@Component({
  selector: 'app-timesheet-edit',
  templateUrl: './timesheet-edit.component.html',
  // styleUrls: ['./timesheet-edit.component.css']
})
export class TimesheetEditComponent implements OnInit {

  timesheetModel: Timesheet = new Timesheet();
  timesheetCodeList: TimesheetCode[];

  constructor(
    private timesheetService: TimesheetService, 
    private timesheetCodeService: TimesheetCodeService,
    private router: Router, 
    private route: ActivatedRoute) { }

  ngOnInit() {
    this.getTimesheet();
    this.timesheetCodeService.getTimesheetCodeList().subscribe(data => this.timesheetCodeList = data);
  }

  getTimesheet(): void {
    const timesheetId: string = this.route.snapshot.paramMap.get('timesheetId');
    this.timesheetService.getTimesheet(timesheetId)
      .subscribe(
        data => this.timesheetModel = data
      );
  }

  updateTimesheet(): void{
    this.timesheetService.updateTimesheet(this.timesheetModel)
      .subscribe(
        _ => {this.router.navigate(['/Timesheet']);}
      );
  }
}
