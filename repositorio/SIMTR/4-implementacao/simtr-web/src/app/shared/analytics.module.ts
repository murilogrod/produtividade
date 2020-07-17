import { NgModule } from "@angular/core";
import { AnalyticsEventDirective } from "./directives/analytics-event/analytics-event.directive";
import { AnalyticsPageDirective } from "./directives/analytics-page/analytics-page.directive";
import { AnalyticsService } from "../services/analytics/analytics.service";

@NgModule({
  declarations: [
    AnalyticsPageDirective,
    AnalyticsEventDirective
  ],
  providers:[AnalyticsService],
  exports: [
    AnalyticsPageDirective,
    AnalyticsEventDirective
  ]
})
export class AnalyticsModule {}
