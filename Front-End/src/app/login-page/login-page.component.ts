import { Component } from '@angular/core';
import { FormBuilder,FormGroup,Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.css'
})
export class LoginPageComponent {
  loginForm !:FormGroup;

  constructor(private fb:FormBuilder,private http:HttpClient){}

  ngOnInit(){
    this.loginForm = this.fb.group({
      email:['',Validators.required],
      password:['',Validators.required],
    });
  }

  onSubmit(){
    if(this.loginForm.valid){
      const credentials = this.loginForm.value;
      console.log(credentials);
    }
  }
}
