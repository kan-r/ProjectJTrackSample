import { Component, OnInit } from '@angular/core';
import { User } from './user';
import { UserService } from './user.service';
import { AuthService } from '../auth/auth.service'; 

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  // styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  userList: User[];
  isCurrentUserAdmin = false;

  constructor(
    private userService: UserService, 
    private authService: AuthService) { }

  ngOnInit() {
    this.userService.getUserList().subscribe(data => this.userList = data);
    let currentUser = this.authService.getAppUser();
    this.isCurrentUserAdmin = (currentUser === 'ADMIN');
  }

  deleteUser(userId: string): void {
    this.userService.deleteUser(userId)
      .subscribe(
        _ => {this.userService.getUserList().subscribe(data => this.userList = data);}
      );
  }

}
