import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DigitalizacaoScannerComponent } from './digitalizacao-scanner.component';

describe('DigitalizacaoScannerComponent', () => {
  let component: DigitalizacaoScannerComponent;
  let fixture: ComponentFixture<DigitalizacaoScannerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DigitalizacaoScannerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DigitalizacaoScannerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
