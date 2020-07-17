import { QuestionBase } from './question-base';

export class MonetarioQuestion extends QuestionBase<string> {
  controlType = 'monetario';
  type: string;

  constructor(options: {} = {}) {
    super(options);
    this.type = options['type'] || '';
  }
}