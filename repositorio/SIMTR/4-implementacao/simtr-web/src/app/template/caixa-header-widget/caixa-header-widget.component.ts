import { Component, OnInit , Input} from '@angular/core';
import {Router} from '@angular/router';
import {ApplicationService} from '../../services';

@Component({
  selector: 'cx-header-widget',
  templateUrl: './caixa-header-widget.component.html',
  styleUrls: ['./caixa-header-widget.component.css']
})
export class CaixaHeaderWidgetComponent implements OnInit {

  config : any = {}
  
  constructor(private appService: ApplicationService,
    private router : Router) { 
  }

  ngOnInit() {
    let name = this.router.url.substring(1);
    if(name.indexOf("/") != -1){
      name = name.substring(0, name.indexOf("/"));
    }
    this.config = this.appService.getWidgetsProperties(name);
  }
  
}
