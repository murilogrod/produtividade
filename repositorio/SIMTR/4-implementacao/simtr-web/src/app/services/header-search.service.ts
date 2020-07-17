import { EVENTS } from '../constants/constants';
import { EventService } from './event-service/event-service';
import { AlertMessageService } from './message/alert-message.service';

export abstract class HeaderSearchService  extends AlertMessageService{

  constructor(private evService : EventService) {
    super();
    this.evService.on(EVENTS.headerSearch, this.headerSearch);
    this.evService.on(EVENTS.cleanHeaderSearch, this.cleanHeaderSearch);
  }

  abstract headerSearch(filtros);

  abstract cleanHeaderSearch();

}
