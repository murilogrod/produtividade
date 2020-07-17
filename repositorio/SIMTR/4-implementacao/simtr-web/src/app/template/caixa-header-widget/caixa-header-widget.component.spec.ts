import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CaixaHeaderWidgetComponent } from './caixa-header-widget.component';

describe('CaixaHeaderWidgetComponent', () => {
  let component: CaixaHeaderWidgetComponent;
  let fixture: ComponentFixture<CaixaHeaderWidgetComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CaixaHeaderWidgetComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CaixaHeaderWidgetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
