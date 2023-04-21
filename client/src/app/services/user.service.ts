import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { lastValueFrom } from 'rxjs';
import { User, UserSearchResult } from '../models/models';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private http: HttpClient) {}

  user!: User;

  postNewUser(user: User) {
    const body = {
      username: user.username,
      email: user.email,
      password: user.password,
    };

    return lastValueFrom(this.http.post('/api/register', body));
  }

  checkIfUserExists(
    username: string,
    email: string
  ): Promise<UserSearchResult> {
    const params = new HttpParams()
      .set('username', username)
      .set('email', email);

    return lastValueFrom(this.http.get<UserSearchResult>('/api/user/search', { params: params }));
  }
}
