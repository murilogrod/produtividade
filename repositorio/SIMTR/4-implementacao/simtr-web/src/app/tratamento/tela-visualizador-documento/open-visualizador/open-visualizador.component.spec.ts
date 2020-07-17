import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OpenVisualizadorComponent } from './open-visualizador.component';

describe('OpenVisualizadorComponent', () => {
  let component: OpenVisualizadorComponent;
  let fixture: ComponentFixture<OpenVisualizadorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OpenVisualizadorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OpenVisualizadorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
