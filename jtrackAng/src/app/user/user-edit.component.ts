import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { User } from './user';
import { UserService } from './user.service';

@Component({
  selector: 'app-user-edit',
  templateUrl: './user-edit.component.html',
  // styleUrls: ['./user-edit.component.css']
})
export class UserEditComponent implements OnInit {

  userModel: User = new User();

  constructor(private userService: UserService, private router: Router, private route: ActivatedRoute) { }

  ngOnInit() {
    this.getUser();
  }

  getUser(): void {
    const userId: string = this.route.snapshot.paramMap.get('userId');
    this.userService.getUser(userId)
      .subscribe(
        data => this.userModel = data
      );
  }

  updateUser(): void{
    this.userService.updateUser(this.userModel)
      .subscribe(
        _ => {this.router.navigate(['/User']);}
      );
  }

}
