import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-toolbar',
  templateUrl: './toolbar.component.html',
  styleUrls: ['./toolbar.component.css'],
})
export class ToolbarComponent {

  constructor(private userService: UserService, private router: Router) {}

  logout() {
    this.userService.isLoggedIn = false;
    this.router.navigate(['/']); 
  }
}
