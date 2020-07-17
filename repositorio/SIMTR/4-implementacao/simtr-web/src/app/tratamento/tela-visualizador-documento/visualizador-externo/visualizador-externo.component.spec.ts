import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VisualizadorExternoComponent } from './visualizador-externo.component';

describe('VisualizadorExternoComponent', () => {
  let component: VisualizadorExternoComponent;
  let fixture: ComponentFixture<VisualizadorExternoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VisualizadorExternoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VisualizadorExternoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
