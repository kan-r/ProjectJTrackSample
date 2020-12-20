import { User } from '../user/user';

export class JobStatus {
    jobStatus: string;
    jobStatusDesc: string;
    active: boolean = true;
    dateCrt: Date;
    userCrt: string;
    dateMod: Date;
    userMod: string;
    userCrtObj: User;
    userModObj: User;
}
