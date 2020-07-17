import {Component, OnInit, AfterViewInit} from '@angular/core';
import {HttpErrorResponse, HttpClient} from '@angular/common/http';
import {ApplicationService, EventService, AuthenticationService} from '../../services/index';
import {environment} from '../../../environments/environment';
import {EVENTS} from '../../constants/constants';

@Component({
  selector: 'cx-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css']
})
export class FooterComponent implements OnInit , AfterViewInit{


  app: any = {}
  instancia: string;

  constructor(private appService: ApplicationService, 
    private http: HttpClient,
    private eventService: EventService,
    private authService : AuthenticationService) {}

  ngOnInit() {
    this.app = this.appService.getApp();
    
  }
  
  ngAfterViewInit(): void {
    this.eventService.on(EVENTS.authorization, auth => {
      this.getInstancia();
    });

    if (this.authService.usuarioAutenticado()) {
      this.getInstancia();
    }
  }

  private getInstancia() {
    this.http.get(environment.serverPath + '/servidor/instancia').subscribe(data => {
      this.instancia = data['nomeInstancia'];
    });
  }

}
