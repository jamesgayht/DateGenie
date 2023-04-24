import { AfterViewInit, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { UserService } from '../services/user.service';
import { User, UserSearchResult } from '../models/models';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit, AfterViewInit {
  form!: FormGroup;
  username!: string;
  password!: string;
  userSearchResult!: UserSearchResult;
  isLoading: boolean = false; 
  forTestingUser = "admin";
  forTestingPass = "asdf1234";

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private userService: UserService,
    public snackBar: MatSnackBar
  ) {}

  ngAfterViewInit(): void {
    if(!this.userService.isLoggedIn) this.loggingOut(); 
  }

  ngOnInit(): void {
    this.form = this.createForm();
    this.userService.isLoggedIn = true;
  }

  createForm(): FormGroup {
    return this.fb.group({
      username: this.fb.control('', [
        Validators.required,
        Validators.minLength(3),
      ]),
      password: this.fb.control('', [
        Validators.required,
        Validators.minLength(3),
      ]),
    });
  }

  verifyUser() {
    this.isLoading = true; 
    this.username = this.form.get('username')?.value;
    this.password = this.form.get('password')?.value;

    try {
      this.userService.checkIfUserExists(this.username).then((result) => {
        this.userSearchResult = result;
        let config = new MatSnackBarConfig();
        config.duration = 3000;
        if (this.userSearchResult.resp !== 'user exists') {
          const invalidMessage: string = `User does not exist!`;
          this.snackBar.open(invalidMessage, 'Close', config);
          this.form.reset();

        } else if (this.userSearchResult.resp === 'user exists') {
          this.userService
            .checkLoginCredentials(this.username, this.password)
            .then((result) => {
              if (result.resp === 'login ok') {
                const welcomeMessage: string = `Welcome back ${this.username}!`;
                this.snackBar.open(welcomeMessage, 'Close', config);
                this.userService.username = this.username;
                this.router.navigate(['/home']);
              }
              else {
                const invalidPassword: string = `Invalid Password!`;
                this.snackBar.open(invalidPassword, 'Close', config);
                this.form.reset();
              }
            });
        }
      });
    } catch (error) {
      console.info('login error >>> ', error);
    }
    this.isLoading = false;
  }

  loggingOut() {
    localStorage.clear();
    window.location.reload();
  }
}
