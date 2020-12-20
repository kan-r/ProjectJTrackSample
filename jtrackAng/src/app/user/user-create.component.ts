import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from './user';
import { UserService } from './user.service';

@Component({
  selector: 'app-user-create',
  templateUrl: './user-create.component.html',
  // styleUrls: ['./user-create.component.css']
})
export class UserCreateComponent implements OnInit {

  userModel: User = new User();

  constructor(private userService: UserService, private router: Router) { }

  ngOnInit() {
  }

  addUser(): void{
    this.userService.addUser(this.userModel)
      .subscribe(
        _ => {this.router.navigate(['/User']);}
      );
  }

}
