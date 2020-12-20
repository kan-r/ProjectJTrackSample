import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  // styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  user: string;
  password: string;
  successUrl: string = "/Job";

  constructor(private authService: AuthService, private router: Router, private route: ActivatedRoute) { }

  ngOnInit() {
    let successUrl = this.route.snapshot.paramMap.get('successUrl');
    if(successUrl !== null){
      this. successUrl = successUrl;
    }
  }

  logIn(): void {
    this.authService.authenticate(this.user, this.password, this.successUrl);
  }
}
