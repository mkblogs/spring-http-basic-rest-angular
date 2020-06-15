import { Injectable } from '@angular/core';
import { environment } from './../../../environments/environment';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { User, ChangePassword } from 'src/app/model/model';

@Injectable({
  providedIn: 'root'
})
/* tslint:disable */
export class UserService {

  private userURL   = environment.apiUrl + "/api/admin/user";
  private changePwd = environment.apiUrl + "/api/admin/user/changepassword";
  private loginURL  = environment.apiUrl + "/api/token"

  constructor(private http: HttpClient) { }

  public saveData(user: User): Observable<any> {    
    return this.http.post(this.userURL,user);
  }

  public updateData(user: User): Observable<any> {    
    return this.http.put(this.userURL,user);
  }

  public getData(userId: number): Observable<any> {
    return this.http.get(this.userURL+"/"+userId);
  }

  public deleteData(userId: number): Observable<any> {
    return this.http.delete(this.userURL+"/"+userId);
  }

  public changePassword(changePassword: ChangePassword): Observable<any> {
    return this.http.post(this.changePwd,changePassword);
  }

   public getFilterData(user: User): Observable<any> {
    const httpParams = new HttpParams()
                        .append("userName",user.loginName)
                        .append("firstName",user.firstName)
                        .append("lastName",user.lastName);
                        
    const params = {
      params : httpParams,
    };                      
    return this.http.get(this.userURL,params);
  }
 
  public login(username: string, password: string ): Observable<any> { 
    const headers = new HttpHeaders({ Authorization: 'Basic ' + btoa(username + ':' + password) });
    const authURL = this.loginURL + "?loginName="+username+"&password="+password;
    console.log(authURL);
    const user = {
      loginName: username,
      password: password,
    };
    console.log(user);
    console.log(headers);
    return this.http.post(authURL,user,{ headers });
  }
}
