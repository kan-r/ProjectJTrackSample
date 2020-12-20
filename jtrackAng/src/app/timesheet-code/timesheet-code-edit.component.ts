import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { TimesheetCode } from './timesheet-code';
import { TimesheetCodeService } from './timesheet-code.service';

@Component({
  selector: 'app-timesheet-code-edit',
  templateUrl: './timesheet-code-edit.component.html',
  // styleUrls: ['./timesheet-code-edit.component.css']
})
export class TimesheetCodeEditComponent implements OnInit {

  timesheetCodeModel: TimesheetCode = new TimesheetCode();

  constructor(private timesheetCodeService: TimesheetCodeService, private router: Router, private route: ActivatedRoute) { }

  ngOnInit() {
    this.getTimesheetCode();
  }

  getTimesheetCode(): void {
    const timesheetCode: string = this.route.snapshot.paramMap.get('timesheetCode');
    this.timesheetCodeService.getTimesheetCode(timesheetCode)
      .subscribe(
        data => this.timesheetCodeModel = data
      );
  }

  updateTimesheetCode(): void{
    this.timesheetCodeService.updateTimesheetCode(this.timesheetCodeModel)
      .subscribe(
        _ => {this.router.navigate(['/TimesheetCode']);}
      );
  }
}
