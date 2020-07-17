import { Component, OnInit } from '@angular/core';
import { DossieService } from 'src/app/dossie/dossie-service';
import { URL_CACH } from 'src/app/constants/constants';

@Component({
  selector: 'app-perfil-usuario',
  templateUrl: './perfil-usuario.component.html',
  styleUrls: ['./perfil-usuario.component.css']
})
export class PerfilUsuarioComponent implements OnInit {

  constructor(private dossieService: DossieService) { }

  ngOnInit() {
    this.dossieService.buscarPatriarcar(URL_CACH.DASHBOARD);
  }

}
