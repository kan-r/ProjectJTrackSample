import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  currentUser: string = "";
  isCurrentUserAdmin = false;

  constructor(private authService: AuthService) { }

  ngOnInit() {
    this.currentUser = this.authService.getAppUser();
    this.isCurrentUserAdmin = (this.currentUser === 'ADMIN');

    if(this.currentUser != null){
      return;
    }

    this.authService.currentUserObservable
      .subscribe(
        (user: string) => {
          this.currentUser = user.toUpperCase();
          this.isCurrentUserAdmin = (this.currentUser === 'ADMIN');
        }
      );
  }
}