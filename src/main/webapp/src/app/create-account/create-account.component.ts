import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormGroup, FormControl, FormBuilder, Validators } from '@angular/forms';
import { AccountServiceService } from '../services/account/account-service.service';
import { Account, ResponseDTO, Error } from '../model/model';
import { AlertService } from '../alert';
import { Router } from '@angular/router';

@Component({
  selector: 'app-create-account',
  templateUrl: './create-account.component.html',
  styleUrls: ['./create-account.component.css']
})
/* tslint:disable */
export class CreateAccountComponent implements OnInit,OnDestroy {

  toggle: boolean = false;
  responseObject : ResponseDTO<Account,Error[]> = null;
  loading: boolean = false;

  editAccount: Account = null;

  //Form Related
  accountForm: FormGroup;
  accountName: FormControl;
  accountType: FormControl;
  accountAmount: FormControl;
  submitted = false;

  constructor(private formBuilder: FormBuilder,
              private accountService: AccountServiceService,
              protected alertService: AlertService,
              private router: Router) { }

  ngOnInit() {
    if(history.state.accountObject){
      this.editAccount = history.state.accountObject;
      this.ngOnInitFormBuilderForUpdate(this.editAccount);
    }else{
      this.ngOnInitFormBuilder();
    }    
  }

  get f() { return this.accountForm.controls; }

  ngOnInitFormBuilder() {
    this.accountName = new FormControl('',[Validators.required]);
    this.accountType = new FormControl('',[Validators.required]);
    this.accountAmount = new FormControl('',[Validators.required]);
    this.accountForm = this.formBuilder.group({
          accountName: this.accountName,
          accountType: this.accountType,
          accountAmount: this.accountAmount
      });
      this.accountAmount.setErrors({});
      this.accountName.setErrors({});
  }

  ngOnInitFormBuilderForUpdate(editAccount: Account) {
    this.accountName = new FormControl(editAccount.name,[Validators.required]);
    this.accountType = new FormControl(editAccount.type,[Validators.required]);
    this.accountAmount = new FormControl(editAccount.amount,[Validators.required]);
    this.accountForm = this.formBuilder.group({
          accountName: this.accountName,
          accountType: this.accountType,
          accountAmount: this.accountAmount
    });
    this.accountAmount.setErrors({});
    this.accountName.setErrors({});
  }

  saveAccountData() {
    this.submitted = true;
    let saveAccount: Account = this.constructFormModelObject();
    this.accountService.saveData(saveAccount).subscribe(data => {
      this.responseObject = JSON.parse(JSON.stringify(data));      
      const isError = this.responseObject.isError;
      if(isError){
         var list: Error[] = JSON.parse(JSON.stringify(this.responseObject.errorObject));
         this.constructErrorObject(list);
      }else{
         this.router.navigate(['/succ-account']);
      }
   });   
  }

  constructErrorObject (errorList: Error[] ) {
    for(var errorObject of errorList){     
     if("amount" == errorObject.errorCode){
       this.accountAmount.setErrors({"notValidInput":errorObject.errorMsg});
     }
     if("UniqueValue" == errorObject.errorCode){
       this.accountName.setErrors({"notValidInput":errorObject.errorMsg});
     }
     // console.log(errorObject.errorCode);
    }
  }

  clickEvent() {
    this.toggle = !this.toggle;
  }

  constructFormModelObject() : Account {
    let accountModel: Account = {
      name : this.accountName.value,
      type : this.accountType.value,
      amount : this.accountAmount.value
    };
    return  accountModel;
  }

  updateAccountData() {
    this.submitted = true;
    this.editAccount.name = this.accountName.value;
    this.editAccount.type = this.accountType.value;
    this.editAccount.amount = this.accountAmount.value;
    console.log(this.editAccount);
    this.accountService.updateData(this.editAccount).subscribe(data => {
      console.log(data);
      this.responseObject = JSON.parse(JSON.stringify(data));      
      const isError = this.responseObject.isError;
      if(isError){
         var list: Error[] = JSON.parse(JSON.stringify(this.responseObject.errorObject));
         this.constructErrorObject(list);
      }else{
         this.router.navigate(['/succ-account']);
      }
   });   
  }

  gotoWelcome(): void {
     this.router.navigate(['/welcome']);
  }

   ngOnDestroy(): void {
    this.editAccount = null;
  }
}
