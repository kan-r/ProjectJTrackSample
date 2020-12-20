import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

import { ConfigService } from '../config/config.service';
import { MessageService } from '../message/message.service';
import { AuthService } from '../auth/auth.service';
import { Timesheet, TimesheetSO } from './timesheet';
import { isNullOrUndefined } from 'util';

@Injectable({
  providedIn: 'root'
})
export class TimesheetService {

  constructor(
    private httpClient: HttpClient, 
    private authService: AuthService, 
    private messageService: MessageService) { }

    private baseUrl: string = ConfigService.baseUrl + "/timesheets";

    getTimesheetList(): Observable<Timesheet[]>{
      this.clearMessage();
      return this.httpClient.get<Timesheet[]>(this.baseUrl, this.authService.getHttpOptions())
        .pipe(
          tap(_ => this.log('Got Timesheet  list')),
          catchError(this.handleError<Timesheet[]>('Get Timesheet  list', []))
        );
    }

    getTimesheetList2(timesheetSO: TimesheetSO): Observable<Timesheet[]> {
      this.clearMessage();

      if(isNullOrUndefined(timesheetSO.userId)){
        timesheetSO.userId = '';
      }

      let workedDateFrom = '';
      if (!isNullOrUndefined(timesheetSO.workedDateFrom)){
        workedDateFrom = timesheetSO.workedDateFrom.toString();
      }

      let workedDateTo = '';
      if (!isNullOrUndefined(timesheetSO.workedDateTo)){
        workedDateFrom = timesheetSO.workedDateTo.toString();
      }

      let url = `${this.baseUrl}?userId=${timesheetSO.userId}&workedDateFrom=${workedDateFrom}&workedDateTo=${workedDateTo}`;

      return this.httpClient.get<Timesheet[]>(url, this.authService.getHttpOptions())
        .pipe(
          tap(_ => this.log('Got Timesheet  list')),
          catchError(this.handleError<Timesheet[]>('Get Timesheet  list', []))
        );
    }
  
    getTimesheet(timesheetId: string): Observable<Timesheet>{
      this.clearMessage();
      const url = `${this.baseUrl}/${timesheetId}`;
  
      return this.httpClient.get<Timesheet>(url, this.authService.getHttpOptions())
        .pipe(
          tap((timesheet: Timesheet) => this.log(`Got Timesheet  ${timesheet.timesheetId}`)),
          catchError(this.handleError<Timesheet>('Get Timesheet '))
        );
    }
  
    addTimesheet(timesheet: Timesheet): Observable<Timesheet> {
      if(timesheet.userId == null || timesheet.userId.trim() === ''){
          this.logError('User is required');
          return throwError('User is required');
      }

      if(timesheet.jobNo == null){
        this.logError('Job is required');
        return throwError('Job is required');
      }

      if(timesheet.workedDate == null){
        this.logError('Worked Date is required');
        return throwError('Worked Date is required');
      }

      this.clearMessage();
      return this.httpClient.post<Timesheet>(this.baseUrl, timesheet, this.authService.getHttpOptions())
        .pipe(
          tap((newTimesheet: Timesheet) => this.log(`Created Timesheet  ${newTimesheet.timesheetId}`)),
          catchError(this.handleError<Timesheet>(`Create Timesheet`))
        );
    }
  
    updateTimesheet(timesheet: Timesheet): Observable<Timesheet> {
      this.clearMessage();
      const url = `${this.baseUrl}/${timesheet.timesheetId}`;

      return this.httpClient.put<Timesheet>(url, timesheet, this.authService.getHttpOptions())
        .pipe(
          tap((newTimesheet: Timesheet) => this.log(`Updated Timesheet  ${newTimesheet.timesheetId}`)),
          catchError(this.handleError<Timesheet>(`Update Timesheet  ${timesheet.timesheetId})`))
        );
    }
  
    deleteTimesheet(timesheetId: string): Observable<Object> {
      this.clearMessage();
      const url = `${this.baseUrl}/${timesheetId}`;
  
      return this.httpClient.delete<Object>(url, this.authService.getHttpOptions())
        .pipe(
          tap(_ => this.log(`Deleted Timesheet  ${timesheetId}`)),
          catchError(this.handleError<Object>(`Delete Timesheet  ${timesheetId}`))
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
