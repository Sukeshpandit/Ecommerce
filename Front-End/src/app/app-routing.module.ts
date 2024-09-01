import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginPageComponent } from './login-page/login-page.component';
import { SignupPageComponent } from './signup-page/signup-page.component';

const routes: Routes = [
  {path : 'login' , component : LoginPageComponent},
  {path : 'signup', component : SignupPageComponent},
  { path: '', redirectTo: '/signup', pathMatch: 'full'},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
