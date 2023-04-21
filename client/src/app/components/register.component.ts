import { Component, OnInit } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { User, UserSearchResult } from '../models/models';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent implements OnInit {
  form!: FormGroup;
  username!: string;
  email!: string;
  password!: string;
  passwordValidator!: string;
  userSearchResult!: UserSearchResult;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private userService: UserService,
    public snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.form = this.createForm();
    this.form.addValidators(
      this.createCompareValidator(
        this.form.get('password')?.value,
        this.form.get('passwordValidator')?.value
      )
    );
  }

  createForm(): FormGroup {
    return this.fb.group({
      username: this.fb.control('', [
        Validators.required,
        Validators.minLength(3),
      ]),
      email: this.fb.control('', [Validators.required, Validators.email]),
      password: this.fb.control('', [
        Validators.required,
        Validators.minLength(3),
      ]),
      passwordValidator: this.fb.control('', [
        Validators.required,
        Validators.minLength(3),
      ]),
    });
  }

  registerUser() {
    this.username = this.form.get('username')?.value;
    this.email = this.form.get('email')?.value;
    this.password = this.form.get('password')?.value;
    this.passwordValidator = this.form.get('passwordValidator')?.value;

    const user = new User(this.username, this.email, this.password);
    console.info("user >>> ", user);

    try {
      this.userService.checkIfUserExists(this.username, this.email).then((result) => {
        this.userSearchResult = result; 
        console.log("userSearchResult >>> ", this.userSearchResult);
        let config = new MatSnackBarConfig();
        config.duration = 3000; 
        if(this.password !== this.passwordValidator) {
          const invalidMessage: string = `Passwords do not match!`;
          this.snackBar.open(invalidMessage, "Close", config);
        }
        else if(this.userSearchResult.resp === "user exists") {
          const invalidMessage: string = `User already exists!`;
          this.snackBar.open(invalidMessage, "Close", config);
        }
        else {
          const welcomeMessage: string = `Welcome ${user.username}!`;
          this.snackBar.open(welcomeMessage, "Close", config);
          this.userService.postNewUser(user);
          this.router.navigate(['/']);
        }
      })
    } catch (error) {
      console.info("error >>> ", error);
    }
  }

  createCompareValidator(
    controlOne: AbstractControl,
    controlTwo: AbstractControl
  ) {
    return () => {
      if (controlOne.value !== controlTwo.value)
        return { match_error: 'Value does not match' };
      return null;
    };
  }
}
