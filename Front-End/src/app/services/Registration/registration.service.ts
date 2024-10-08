import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {
  private apiUrl = 'http://localhost:8080/api/v1/user/register'; 
  constructor(private http: HttpClient) { }
  registerUser(data: { email: string; username: string; password: string }): Observable<any> {
    return this.http.post(this.apiUrl, data);
  }
}
