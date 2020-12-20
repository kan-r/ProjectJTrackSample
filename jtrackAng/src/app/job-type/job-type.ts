import { User } from '../user/user';

export class JobType {
    jobType: string;
    jobTypeDesc: string;
    active: boolean = true;
    dateCrt: Date;
    userCrt: string;
    dateMod: Date;
    userMod: string;
    userCrtObj: User;
    userModObj: User;
}
