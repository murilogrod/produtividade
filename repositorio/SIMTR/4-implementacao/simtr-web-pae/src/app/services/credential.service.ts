import { ApplicationService } from './application/application.service';

export abstract class CredentialService {
  constructor(private applicationService: ApplicationService) {}

  hasCredential(funcionalidade: string, acao: string) {
    return this.applicationService.hasCredential(funcionalidade, acao);
  }
}
