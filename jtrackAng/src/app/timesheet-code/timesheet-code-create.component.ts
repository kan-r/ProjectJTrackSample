import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TimesheetCode } from './timesheet-code';
import { TimesheetCodeService } from './timesheet-code.service';

@Component({
  selector: 'app-timesheet-code-create',
  templateUrl: './timesheet-code-create.component.html',
  // styleUrls: ['./timesheet-code-create.component.css']
})
export class TimesheetCodeCreateComponent implements OnInit {

  timesheetCodeModel: TimesheetCode = new TimesheetCode();

  constructor(private timesheetCodeService: TimesheetCodeService, private router: Router) { }

  ngOnInit() {
  }

  addTimesheetCode(): void{
    this.timesheetCodeService.addTimesheetCode(this.timesheetCodeModel)
      .subscribe(
        _ => {this.router.navigate(['/TimesheetCode']);}
      );
  }
}
