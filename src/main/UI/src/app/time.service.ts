import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class TimeService {

  constructor(private httpclient:HttpClient) { }

  getTime():Observable<any>{
    return this.httpclient.get("http://localhost:8080/time")
  }
}
