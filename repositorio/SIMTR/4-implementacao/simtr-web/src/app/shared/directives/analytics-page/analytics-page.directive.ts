import { Directive } from '@angular/core';
import { Router,NavigationEnd } from '@angular/router';
import { AnalyticsService }  from '../../../services/analytics/analytics.service';

@Directive({
  selector: '[analytics-page]'
})
export class AnalyticsPageDirective {
  
	constructor(private router: Router, private analytics: AnalyticsService) { 
        
    if (router.url && router.url.length>0) {
      analytics.trackPage(router.url, null);
    }
    
  }
  
  
}