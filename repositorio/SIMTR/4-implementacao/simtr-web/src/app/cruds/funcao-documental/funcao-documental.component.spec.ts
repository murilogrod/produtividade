import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FuncaoDocumentalComponent } from './funcao-documental.component';

describe('MetaprocessoComponent', () => {
  let component: FuncaoDocumentalComponent;
  let fixture: ComponentFixture<FuncaoDocumentalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FuncaoDocumentalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FuncaoDocumentalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
