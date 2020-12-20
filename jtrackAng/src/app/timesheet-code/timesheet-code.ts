import { User } from '../user/user';

export class TimesheetCode {
    timesheetCode: string;
    timesheetCodeDesc: string;
    active: boolean = true;
    dateCrt: Date;
    userCrt: string;
    dateMod: Date;
    userMod: string;
    userCrtObj: User;
    userModObj: User;
}
