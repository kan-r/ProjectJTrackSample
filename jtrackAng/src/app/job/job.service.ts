import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

import { ConfigService } from '../config/config.service';
import { MessageService } from '../message/message.service';
import { AuthService } from '../auth/auth.service';
import { Job, JobSO } from './job';

@Injectable({
  providedIn: 'root'
})
export class JobService {

  constructor(
    private httpClient: HttpClient, 
    private authService: AuthService, 
    private messageService: MessageService) { }

    private baseUrl: string = ConfigService.baseUrl + "/jobs";

    getJobList(): Observable<Job[]>{
      this.clearMessage();
      return this.httpClient.get<Job[]>(this.baseUrl, this.authService.getHttpOptions())
        .pipe(
          tap(_ => this.log('Got Job  list')),
          catchError(this.handleError<Job[]>('Get Job  list', []))
        );
    }

    getJobList2(jobSO: JobSO): Observable<Job[]>{
      this.clearMessage();
      const url = `${this.baseUrl}?name=${jobSO.jobName}&type=${jobSO.jobType}&status=${jobSO.jobStatus}&assignedTo=${jobSO.assignedTo}&includeChild=${jobSO.includeChildJobs}&nameC=${jobSO.jobNameChild}&typeC=${jobSO.jobTypeChild}&statusC=${jobSO.jobStatusChild}&assignedToC=${jobSO.assignedToChild}`;
      return this.httpClient.get<Job[]>(url, this.authService.getHttpOptions())
        .pipe(
          tap(_ => this.log('Got Job  list')),
          catchError(this.handleError<Job[]>('Get Job  list', []))
        );
    }

    getParentJobList(): Observable<Job[]>{
      this.clearMessage();
      const url: string = this.baseUrl + "?type=Project";
      return this.httpClient.get<Job[]>(url, this.authService.getHttpOptions())
        .pipe(
          tap(_ => this.log('Got Parent Job  list')),
          catchError(this.handleError<Job[]>('Get Parent Job  list', []))
        );
    }
  
    getJob(jobNo: string): Observable<Job>{
      this.clearMessage();
      const url = `${this.baseUrl}/${jobNo}`;
  
      return this.httpClient.get<Job>(url, this.authService.getHttpOptions())
        .pipe(
          tap((job: Job) => this.log(`Got Job  ${job.jobNo}`)),
          catchError(this.handleError<Job>('Get Job '))
        );
    }
  
    addJob(job: Job): Observable<Job> {
      let jobName  = job.jobName;
      if(jobName == null || jobName.trim() === ''){
          this.logError('Job Name is required');
          return throwError('Job Name is required');
      }

      this.clearMessage();
      return this.httpClient.post<Job>(this.baseUrl, job, this.authService.getHttpOptions())
        .pipe(
          tap((newJob: Job) => this.log(`Created Job  ${newJob.jobNo}`)),
          catchError(this.handleError<Job>(`Create Job  ${job.jobNo}`))
        );
    }
  
    updateJob(job: Job): Observable<Job> {
      this.clearMessage();
      const url = `${this.baseUrl}/${job.jobNo}`;

      return this.httpClient.put<Job>(url, job, this.authService.getHttpOptions())
        .pipe(
          tap((newJob: Job) => this.log(`Updated Job  ${newJob.jobNo}`)),
          catchError(this.handleError<Job>(`Update Job  ${job.jobNo})`))
        );
    }
  
    deleteJob(jobNo: string): Observable<Object> {
      this.clearMessage();
      const url = `${this.baseUrl}/${jobNo}`;
  
      return this.httpClient.delete<Object>(url, this.authService.getHttpOptions())
        .pipe(
          tap(_ => this.log(`Deleted Job  ${jobNo}`)),
          catchError(this.handleError<Object>(`Delete Job  ${jobNo}`))
        );
    }
  
    /**
     * Handle Http operation that failed.
     * Let the app continue.
     * @param operation - name of the operation that failedS
     * @param result - optional value to return as the observable result
     */
    private handleError<T> (operation = 'operation', result?: T) {
      // return (error: any): Observable<T> => {
      //   this.logError(`${operation} failed: ${error.error}`);
      //   return throwError(error);
      // };
      return this.messageService.handleError(operation, result);
    }

    private log(message: string) {
      this.messageService.log(message);
    }
  
    private logInfo(info: string) {
      this.messageService.logInfo(info);
    }
  
    private logError(error: any) {
      let err = this.messageService.extractError(error);
      this.messageService.logError(err);
    }
  
    private clearMessage(){
      this.messageService.clearMessage();
    }
}
