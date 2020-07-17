import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GarantiaProdutoComponent } from './garantia-produto.component';

describe('GarantiaProdutoComponent', () => {
  let component: GarantiaProdutoComponent;
  let fixture: ComponentFixture<GarantiaProdutoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GarantiaProdutoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GarantiaProdutoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
