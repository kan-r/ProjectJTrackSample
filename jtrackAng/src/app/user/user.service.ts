import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

import { ConfigService } from '../config/config.service';
import { MessageService } from '../message/message.service';
import { AuthService } from '../auth/auth.service';
import { User } from './user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(
    private httpClient: HttpClient, 
    private authService: AuthService, 
    private messageService: MessageService) { }

  private baseUrl: string = ConfigService.baseUrl + "/users";

  getUserList(): Observable<User[]>{
    this.clearMessage();
    return this.httpClient.get<User[]>(this.baseUrl, this.authService.getHttpOptions())
      .pipe(
        tap(_ => this.log('Got User list')),
        catchError(this.handleError<User[]>('Get User list', []))
      );
  }

  getUser(userId: string): Observable<User>{
    this.clearMessage();
    const url = `${this.baseUrl}/${userId}`;

    return this.httpClient.get<User>(url, this.authService.getHttpOptions())
      .pipe(
        tap((user: User) => this.log(`Got User with User ID = ${user.userId}`)),
        catchError(this.handleError<User>('Get User'))
      );
  }

  addUser(user: User): Observable<User> {
    let t = user.userId;
    if(t == null || t.trim() === ''){
      this.logError('User ID is required');
      return throwError('User ID is required');
    }

    let p = user.pword;
    if(p == null || p.trim() === ''){
      this.logError('Password is required');
      return throwError('Password is required');
    }

    this.clearMessage();
    return this.httpClient.post<User>(this.baseUrl, user, this.authService.getHttpOptions())
      .pipe(
        tap((newUser: User) => this.log(`Created User with User ID = ${newUser.userId}`)),
        catchError(this.handleError<User>(`Create User (with User ID = ${user.userId})`))
      );
  }

  updateUser(user: User): Observable<User> {
    let p = user.pword;
    if(p == null || p.trim() === ''){
      this.logError('Password is required');
      return throwError('Password is required');
    }
    
    this.clearMessage();
    const url = `${this.baseUrl}/${user.userId}`;

    return this.httpClient.put<User>(url, user, this.authService.getHttpOptions())
      .pipe(
        tap((newUser: User) => this.log(`Updated User with User ID = ${newUser.userId}`)),
        catchError(this.handleError<User>(`Update User (with userId = ${user.userId})`))
      );
  }

  deleteUser(userId: string): Observable<Object> {
    this.clearMessage();
    const url = `${this.baseUrl}/${userId}`;

    return this.httpClient.delete<Object>(url, this.authService.getHttpOptions())
      .pipe(
        tap(_ => this.log(`Deleted User with User ID = ${userId}`)),
        catchError(this.handleError<Object>(`Delete User with User ID = ${userId}`))
      );
  }

  /**
   * Handle Http operation that failed.
   * Let the app continue.
   * @param operation - name of the operation that failed
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
