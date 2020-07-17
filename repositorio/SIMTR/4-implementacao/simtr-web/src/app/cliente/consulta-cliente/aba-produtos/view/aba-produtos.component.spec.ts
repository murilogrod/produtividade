import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AbaProdutosComponent } from './aba-produtos.component';

describe('AbaProdutosComponent', () => {
  let component: AbaProdutosComponent;
  let fixture: ComponentFixture<AbaProdutosComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AbaProdutosComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AbaProdutosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
