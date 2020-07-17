import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AbaFormularioComponent } from './aba-formulario.component';

describe('AbaFormularioComponent', () => {
  let component: AbaFormularioComponent;
  let fixture: ComponentFixture<AbaFormularioComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AbaFormularioComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AbaFormularioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
