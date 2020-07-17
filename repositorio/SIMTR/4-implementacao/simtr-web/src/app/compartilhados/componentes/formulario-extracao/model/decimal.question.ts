import { QuestionBase } from './question-base';

export class DecimalQuestion extends QuestionBase<string> {
  controlType = 'decimal';
  type: string;

  constructor(options: {} = {}) {
    super(options);
    this.type = options['type'] || '';
  }
}