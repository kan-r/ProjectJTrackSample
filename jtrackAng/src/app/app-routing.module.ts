import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AuthGuardService } from './auth/auth-guard.service';
import { LoginComponent } from './login/login.component';
import { LogoutComponent } from './logout/logout.component';
import { JobComponent } from './job/job.component';
import { JobCreateComponent } from './job/job-create.component';
import { JobEditComponent } from './job/job-edit.component';
import { TimesheetComponent } from './timesheet/timesheet.component';
import { TimesheetCreateComponent } from './timesheet/timesheet-create.component';
import { TimesheetEditComponent } from './timesheet/timesheet-edit.component';
import { JobTypeComponent } from './job-type/job-type.component';
import { JobTypeCreateComponent } from './job-type/job-type-create.component';
import { JobTypeEditComponent } from './job-type/job-type-edit.component';
import { JobStatusComponent } from './job-status/job-status.component';
import { JobStatusCreateComponent } from './job-status/job-status-create.component';
import { JobStatusEditComponent } from './job-status/job-status-edit.component';
import { JobPriorityComponent } from './job-priority/job-priority.component';
import { JobPriorityCreateComponent } from './job-priority/job-priority-create.component';
import { JobPriorityEditComponent } from './job-priority/job-priority-edit.component';
import { JobResolutionComponent } from './job-resolution/job-resolution.component';
import { JobResolutionCreateComponent } from './job-resolution/job-resolution-create.component';
import { JobResolutionEditComponent } from './job-resolution/job-resolution-edit.component';
import { JobStageComponent } from './job-stage/job-stage.component';
import { JobStageCreateComponent } from './job-stage/job-stage-create.component';
import { JobStageEditComponent } from './job-stage/job-stage-edit.component';
import { TimesheetCodeComponent } from './timesheet-code/timesheet-code.component';
import { TimesheetCodeCreateComponent } from './timesheet-code/timesheet-code-create.component';
import { TimesheetCodeEditComponent } from './timesheet-code/timesheet-code-edit.component';
import { UserComponent } from './user/user.component';
import { UserCreateComponent } from './user/user-create.component';
import { UserEditComponent } from './user/user-edit.component';


const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'Login', component: LoginComponent},
  { path: 'Login/:successUrl', component: LoginComponent},
  { path: 'Logout', component: LogoutComponent},
  { path: 'Job', component: JobComponent, canActivate:[AuthGuardService] },
  { path: 'JobCreate', component: JobCreateComponent, canActivate:[AuthGuardService] },
  { path: 'JobEdit/:jobNo', component: JobEditComponent, canActivate:[AuthGuardService] },
  { path: 'Timesheet', component: TimesheetComponent, canActivate:[AuthGuardService] },
  { path: 'TimesheetCreate', component: TimesheetCreateComponent, canActivate:[AuthGuardService] },
  { path: 'TimesheetEdit/:timesheetId', component: TimesheetEditComponent, canActivate:[AuthGuardService] },
  { path: 'JobType', component: JobTypeComponent, canActivate:[AuthGuardService] },
  { path: 'JobTypeCreate', component: JobTypeCreateComponent, canActivate:[AuthGuardService] },
  { path: 'JobTypeEdit/:jobType', component: JobTypeEditComponent, canActivate:[AuthGuardService] },
  { path: 'JobStatus', component: JobStatusComponent, canActivate:[AuthGuardService] },
  { path: 'JobStatusCreate', component: JobStatusCreateComponent, canActivate:[AuthGuardService] },
  { path: 'JobStatusEdit/:jobStatus', component: JobStatusEditComponent, canActivate:[AuthGuardService] },
  { path: 'JobPriority', component: JobPriorityComponent, canActivate:[AuthGuardService] },
  { path: 'JobPriorityCreate', component: JobPriorityCreateComponent, canActivate:[AuthGuardService] },
  { path: 'JobPriorityEdit/:jobPriority', component: JobPriorityEditComponent, canActivate:[AuthGuardService] },
  { path: 'JobResolution', component: JobResolutionComponent, canActivate:[AuthGuardService] },
  { path: 'JobResolutionCreate', component: JobResolutionCreateComponent, canActivate:[AuthGuardService] },
  { path: 'JobResolutionEdit/:jobResolution', component: JobResolutionEditComponent, canActivate:[AuthGuardService] },
  { path: 'JobStage', component: JobStageComponent, canActivate:[AuthGuardService] },
  { path: 'JobStageCreate', component: JobStageCreateComponent, canActivate:[AuthGuardService] },
  { path: 'JobStageEdit/:jobStage', component: JobStageEditComponent, canActivate:[AuthGuardService] },
  { path: 'TimesheetCode', component: TimesheetCodeComponent, canActivate:[AuthGuardService] },
  { path: 'TimesheetCodeCreate', component: TimesheetCodeCreateComponent, canActivate:[AuthGuardService] },
  { path: 'TimesheetCodeEdit/:timesheetCode', component: TimesheetCodeEditComponent, canActivate:[AuthGuardService] },
  { path: 'User', component: UserComponent, canActivate:[AuthGuardService] },
  { path: 'UserCreate', component: UserCreateComponent, canActivate:[AuthGuardService] },
  { path: 'UserEdit/:userId', component: UserEditComponent, canActivate:[AuthGuardService] }
];

@NgModule({
  // imports: [RouterModule.forRoot(routes)],
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
