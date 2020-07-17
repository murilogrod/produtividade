import { Injectable } from '@angular/core';
import { CanDeactivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';



export interface CanComponenteDeactivate {
  canDeactivate: (nextState?: RouterStateSnapshot) => Observable<boolean> | Promise<boolean> | boolean;
}

@Injectable({
  providedIn: 'root'
})
export class CanDeactivateGuard implements CanDeactivate<CanComponenteDeactivate> {
  canDeactivate(component : CanComponenteDeactivate,
    
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot,
    nextState?: RouterStateSnapshot) {
    return component.canDeactivate ? component.canDeactivate(nextState) : true;
  }
 
}
