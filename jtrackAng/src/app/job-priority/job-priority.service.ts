import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

import { ConfigService } from '../config/config.service';
import { MessageService } from '../message/message.service';
import { AuthService } from '../auth/auth.service';
import { JobPriority } from './job-priority';

@Injectable({
  providedIn: 'root'
})
export class JobPriorityService {

  constructor(
    private httpClient: HttpClient, 
    private authService: AuthService, 
    private messageService: MessageService) { }

    private baseUrl: string = ConfigService.baseUrl + "/jobPriorities";

    getJobPriorityList(): Observable<JobPriority[]>{
      this.clearMessage();
      return this.httpClient.get<JobPriority[]>(this.baseUrl, this.authService.getHttpOptions())
        .pipe(
          tap(_ => this.log('Got Job Priority list')),
          catchError(this.handleError<JobPriority[]>('Get Job Priority list', []))
        );
    }
  
    getJobPriority(jobPriority: string): Observable<JobPriority>{
      this.clearMessage();
      const url = `${this.baseUrl}/${jobPriority}`;
  
      return this.httpClient.get<JobPriority>(url, this.authService.getHttpOptions())
        .pipe(
          tap((jobtype: JobPriority) => this.log(`Got Job Priority ${jobtype.jobPriority}`)),
          catchError(this.handleError<JobPriority>('Get Job Priority'))
        );
    }
  
    addJobPriority(jobPriority: JobPriority): Observable<JobPriority> {
      let j  = jobPriority.jobPriority;
      if(j == null || j.trim() === ''){
          this.logError('Job Priority is required');
          return throwError('Job Priority is required');
      }

      this.clearMessage();
      return this.httpClient.post<JobPriority>(this.baseUrl, jobPriority, this.authService.getHttpOptions())
        .pipe(
          tap((newJobPriority: JobPriority) => this.log(`Created Job Priority ${newJobPriority.jobPriority}`)),
          catchError(this.handleError<JobPriority>(`Create Job Priority ${jobPriority.jobPriority}`))
        );
    }
  
    updateJobPriority(jobPriority: JobPriority): Observable<JobPriority> {
      this.clearMessage();
      const url = `${this.baseUrl}/${jobPriority.jobPriority}`;

      return this.httpClient.put<JobPriority>(url, jobPriority, this.authService.getHttpOptions())
        .pipe(
          tap((newJobPriority: JobPriority) => this.log(`Updated Job Priority ${newJobPriority.jobPriority}`)),
          catchError(this.handleError<JobPriority>(`Update Job Priority ${jobPriority.jobPriority})`))
        );
    }
  
    deleteJobPriority(jobPriority: string): Observable<Object> {
      this.clearMessage();
      const url = `${this.baseUrl}/${jobPriority}`;
  
      return this.httpClient.delete<Object>(url, this.authService.getHttpOptions())
        .pipe(
          tap(_ => this.log(`Deleted Job Priority ${jobPriority}`)),
          catchError(this.handleError<Object>(`Delete Job Priority ${jobPriority}`))
        );
    }
  
    /**
     * Handle Http operation that failed.
     * Let the app continue.
     * @param operation - name of the operation that failedS
     * @param result - optional value to return as the observable result
     */
    private handleError<T> (operation = 'operation', result?: T) {
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
