import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule} from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { NgbModule} from '@ng-bootstrap/ng-bootstrap';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { JobComponent } from './job/job.component';
import { TimesheetComponent } from './timesheet/timesheet.component';
import { UserComponent } from './user/user.component';
import { UserService } from './user/user.service';
import { UserCreateComponent } from './user/user-create.component';
import { UserEditComponent } from './user/user-edit.component';
import { MessageComponent } from './message/message.component';
import { MessageService } from './message/message.service';
import { NavbarComponent } from './navbar/navbar.component';
import { ConfigService } from './config/config.service';
import { LoginComponent } from './login/login.component';
import { AuthService } from './auth/auth.service';
import { AuthGuardService } from './auth/auth-guard.service';
import { LogoutComponent } from './logout/logout.component';
import { JobTypeComponent } from './job-type/job-type.component';
import { JobTypeCreateComponent } from './job-type/job-type-create.component';
import { JobTypeEditComponent } from './job-type/job-type-edit.component';
import { JobStatusComponent } from './job-status/job-status.component';
import { JobPriorityComponent } from './job-priority/job-priority.component';
import { JobResolutionComponent } from './job-resolution/job-resolution.component';
import { JobStageComponent } from './job-stage/job-stage.component';
import { TimesheetCodeComponent } from './timesheet-code/timesheet-code.component';
import { JobCreateComponent } from './job/job-create.component';
import { JobPriorityCreateComponent } from './job-priority/job-priority-create.component';
import { JobResolutionCreateComponent } from './job-resolution/job-resolution-create.component';
import { JobStageCreateComponent } from './job-stage/job-stage-create.component';
import { JobStatusCreateComponent } from './job-status/job-status-create.component';
import { TimesheetCreateComponent } from './timesheet/timesheet-create.component';
import { TimesheetCodeCreateComponent } from './timesheet-code/timesheet-code-create.component';
import { JobEditComponent } from './job/job-edit.component';
import { JobPriorityEditComponent } from './job-priority/job-priority-edit.component';
import { JobResolutionEditComponent } from './job-resolution/job-resolution-edit.component';
import { JobStageEditComponent } from './job-stage/job-stage-edit.component';
import { JobStatusEditComponent } from './job-status/job-status-edit.component';
import { TimesheetEditComponent } from './timesheet/timesheet-edit.component';
import { TimesheetCodeEditComponent } from './timesheet-code/timesheet-code-edit.component';

@NgModule({
  declarations: [
    AppComponent,
    JobComponent,
    TimesheetComponent,
    UserComponent,
    UserCreateComponent,
    UserEditComponent,
    MessageComponent,
    NavbarComponent,
    LoginComponent,
    LogoutComponent,
    JobTypeComponent,
    JobTypeCreateComponent,
    JobTypeEditComponent,
    JobStatusComponent,
    JobPriorityComponent,
    JobResolutionComponent,
    JobStageComponent,
    TimesheetCodeComponent,
    JobCreateComponent,
    JobPriorityCreateComponent,
    JobResolutionCreateComponent,
    JobStageCreateComponent,
    JobStatusCreateComponent,
    TimesheetCreateComponent,
    TimesheetCodeCreateComponent,
    JobEditComponent,
    JobPriorityEditComponent,
    JobResolutionEditComponent,
    JobStageEditComponent,
    JobStatusEditComponent,
    TimesheetEditComponent,
    TimesheetCodeEditComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    NgbModule
  ],
  providers: [
    ConfigService,
    AuthService,
    AuthGuardService,
    MessageService,
    UserService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
