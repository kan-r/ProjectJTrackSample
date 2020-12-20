import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { AuthService } from './auth.service';
import { throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate {

  constructor(private authService: AuthService, private router: Router) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    
    if (this.authService.isUserLoggedIn()){
      
      if(!this.authService.isUserAdmin()){
        if(route.url[0].path === "UserCreate"){
          this.router.navigate(['/User']);
          return false;
        }
        if(route.url[0].path === "UserEdit"){
          this.router.navigate(['/User']);
          return false;
        }
      }

      return true;
    }
    
    this.router.navigate(['/Login/' + state.url]);
    return false;
  }
}
