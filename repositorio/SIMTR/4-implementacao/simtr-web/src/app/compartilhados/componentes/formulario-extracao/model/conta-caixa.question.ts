import { QuestionBase } from './question-base';

export class ContaCaixaQuestion extends QuestionBase<string> {
  controlType = 'conta_caixa';
  type: string;

  constructor(options: {} = {}) {
    super(options);
    this.type = options['type'] || '';
  }
}