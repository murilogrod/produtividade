import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { FormularioGenericoComponent } from './formulario-generico.component';

describe('DocumentoComponent', () => {
  let component: FormularioGenericoComponent;
  let fixture: ComponentFixture<FormularioGenericoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FormularioGenericoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FormularioGenericoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
