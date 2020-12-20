import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { Subject }    from 'rxjs';
import { MessageService } from '../message/message.service';
import { ConfigService } from '../config/config.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private currentUser = new Subject<string>();
  currentUserObservable = this.currentUser.asObservable();

  private SESSION_ATTRIBUTE_USER = 'USER';
  private SESSION_ATTRIBUTE_TOKEN = 'TOKEN';

  private successUrl: string = "/Job";

  constructor(
    private httpClient: HttpClient, 
    private router: Router, 
    private messageService: MessageService) { }

  authenticate(user: string, password: string, successUrl: string): void {
    this.successUrl = successUrl;
    this.obtainAccessToken(user, password);
  }

  isUserLoggedIn(): boolean  {
    return (this.getAccessToken() !== null);
  }

  isUserAdmin(): boolean {
    return (this.getAppUser() === 'ADMIN');
  }

  logOut(): void {
    this.logInfo("Logged out");
    this.removeFromSessionStorage();
    this.router.navigate(['/Login']);
  }

  getHttpOptions(){
    let httpHeaders = 
      new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + this.getAccessToken()
      });

    return { headers: httpHeaders };
  }

  private obtainAccessToken(user: string, password: string): void {

    this.clearMessage();

    let  tokenUrl: string = ConfigService.baseUrl + "/oauth/token";

    let params = new URLSearchParams();
    params.append('username', user);
    params.append('password', password);    
    params.append('grant_type','password');

    let httpHeaders = 
      new HttpHeaders({
        'Content-Type': 'application/x-www-form-urlencoded; charset=utf-8',
        'Authorization': 'Basic ' + btoa("jtrackAdmin:admin")
      });

    let httpOptions = {
      headers: httpHeaders
    };
  
    this.httpClient.post(tokenUrl, params.toString(), httpOptions)
      .subscribe(
        data => { 
          this.addToSessionStorage(user, data);
        },
        error => { 
          // this.logError(error.error.error_description) 
          this.logError(error)
        }
      ); 
  }

  private addToSessionStorage(user: string, token: any): void {
    let  expireDate = new Date().getTime() + (1000 * token.expires_in);
    let sessObj = {
      'access_token': token.access_token, 
      'token_type': token.token_type,
      'expires_at': expireDate,
      'scope': token.scope
    };

    sessionStorage.setItem(this.SESSION_ATTRIBUTE_TOKEN, JSON.stringify(sessObj));
    sessionStorage.setItem(this.SESSION_ATTRIBUTE_USER, user.toUpperCase());

    this.setCurrentUser(user);
  
    this.router.navigate([this.successUrl]);
  }

  private removeFromSessionStorage(): void {
    sessionStorage.removeItem(this.SESSION_ATTRIBUTE_TOKEN);
    sessionStorage.removeItem(this.SESSION_ATTRIBUTE_USER);
    this.setCurrentUser("");
  }

  private setCurrentUser(user: string){
    this.currentUser.next(user);
  }

  private getAccessToken(): string {
    let sessObj = sessionStorage.getItem(this.SESSION_ATTRIBUTE_TOKEN);
    if(sessObj !== null){
      let sessJSON = JSON.parse(sessObj);

      if(new Date().getTime() > sessJSON.expires_at){
        this.logError("Session expired");
        this.logOut();
        return;
      }

      return sessJSON.access_token;
    }
    return null;
  }

  getAppUser(): string {
    return sessionStorage.getItem(this.SESSION_ATTRIBUTE_USER);
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
