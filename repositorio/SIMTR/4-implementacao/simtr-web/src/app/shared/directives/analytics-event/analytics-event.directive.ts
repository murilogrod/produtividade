import { Directive,HostListener,ElementRef } from '@angular/core';
import { AnalyticsService }  from '../../../services/analytics/analytics.service';

@Directive({
  selector: '[analytics-event]'
})
export class AnalyticsEventDirective {
   
  constructor(private analytics: AnalyticsService,private element:ElementRef) {  
    
  }
  
  @HostListener('click') onClick(){

    if(this.element.nativeElement && this.element.nativeElement.id && this.element.nativeElement.tagName){
      this.analytics.trackEvent(this.element.nativeElement.tagName, this.element.nativeElement.id, null);
    }

  }

  
  
}