import { EVENTS } from './../constants/constants';
import { EventService } from './event-service/event-service';

export abstract class HeaderSearchService {

  constructor(private evService : EventService) {
    this.evService.on(EVENTS.headerSearch, this.headerSearch);
    this.evService.on(EVENTS.cleanHeaderSearch, this.cleanHeaderSearch);
  }

  abstract headerSearch(filtros);

  abstract cleanHeaderSearch();

}
