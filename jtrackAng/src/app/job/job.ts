import { User } from '../user/user';

export class Job {
    jobNo: number;
    jobName: string;
    jobDesc: string;
    jobType: string;
    jobPriority: string;
    jobStatus: string;
    jobResolution: string;
    jobStage: string;
    jobOrder: number;
    assignedTo: string;
    timesheetCode: string;
    estimatedHrs: number;
    completedHrs: number;
    estimatedStartDate: Date;
    actualStartDate: Date;
    estimatedEndDate: Date;
    actualEndDate: Date;
    parentJobNo: number;
    active: boolean = true;
    dateCrt: Date;
    userCrt: string;
    dateMod: Date;
    userMod: string;
    jobRef: string;
    parentJobObj: Job;
    assignedToObj: User;
    userCrtObj: User;
    userModObj: User;
}

export class JobSO {
    jobName: string  = "";
    jobType: string = "";
    jobStatus: string = "";
    assignedTo: string = "";
    includeChildJobs: boolean = true;
    jobNameChild: string  = "";
    jobTypeChild: string = "";
    jobStatusChild: string = "";
    assignedToChild: string = "";
}
