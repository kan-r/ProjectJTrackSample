import { Job } from '../job/job';
import { User } from '../user/user';

export class Timesheet {
    timesheetId: string;
    userId: string;
    jobNo: number;
    workedDate: Date;
    workedHrs: number;
    timesheetCode: string;
    active: boolean = true;
    dateCrt: Date;
    userCrt: string;
    dateMod: Date;
    userMod: string;
    jobObj: Job;
    userObj: User;
    userCrtObj: User;
    userModObj: User;
}

export class TimesheetSO {
    userId: string;
    workedDateFrom: Date;
    workedDateTo: Date;
}
