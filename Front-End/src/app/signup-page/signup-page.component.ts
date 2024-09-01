import { Component } from '@angular/core';
import { FormBuilder,FormGroup,Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {RegistrationService} from '../services/Registration/registration.service'
@Component({
  selector: 'app-signup-page',
  templateUrl: './signup-page.component.html',
  styleUrl: './signup-page.component.css'
})
export class SignupPageComponent {
  signupForm !:FormGroup;
  otp:string="";
  correctOtp: string = '123456';
  constructor(private fb:FormBuilder,private http:HttpClient,private registrationService:RegistrationService){}

  ngOnInit(){
    this.signupForm = this.fb.group({
      email:['',Validators.required],
      name:['',Validators.required],
      password:['',Validators.required],
    });
  }

  onSubmit(){
    if(this.signupForm.valid){
      const credentials = this.signupForm .value;
      this.registrationService.registerUser(credentials).subscribe({
        next:response =>{
          console.log("Success:",response);
        },
        error : err =>{
          console.log("Registration failed : ", err);
        }
      });
    }
  }
  verifyOtp() {
    console.log('Entered OTP:', this.otp);

    if (this.otp === this.correctOtp) {
      alert('OTP verified successfully!');
      //this.router.navigate(['/home']); // Navigate to the home page
    } else {
      alert('Invalid OTP. Please try again.');
    }
  }

}
