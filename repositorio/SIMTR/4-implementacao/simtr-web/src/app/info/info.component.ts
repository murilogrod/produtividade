import { Component, OnInit } from '@angular/core';
import { HttpClient } from '../../../node_modules/@angular/common/http';
import { environment } from '../../environments/environment';
import { LoaderService } from '../services';

@Component({
  selector: 'app-info',
  templateUrl: './info.component.html',
  styleUrls: ['./info.component.css']
})
export class InfoComponent implements OnInit {

   informations;

  constructor(private httpclient : HttpClient, private loadService: LoaderService) { }

  ngOnInit() {
    this.httpclient.get(environment.serverPath + '/servidor/information' ).subscribe(res => {
      this.informations = res;
    },
    () => {
      this.loadService.hide();
    }); 
  }

}
