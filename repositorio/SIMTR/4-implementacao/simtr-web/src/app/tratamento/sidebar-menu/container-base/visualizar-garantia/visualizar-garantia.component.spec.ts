import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VisualizarGarantiaComponent } from './visualizar-garantias.component';

describe('VisualizarGarantiasComponent', () => {
  let component: VisualizarGarantiaComponent;
  let fixture: ComponentFixture<VisualizarGarantiaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VisualizarGarantiaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VisualizarGarantiaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
