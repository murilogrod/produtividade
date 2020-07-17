import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VisualizarExternoComponent } from './visualizar-externo.componente';

describe('VisualizarExternoComponent', () => {
  let component: VisualizarExternoComponent;
  let fixture: ComponentFixture<VisualizarExternoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VisualizarExternoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VisualizarExternoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
