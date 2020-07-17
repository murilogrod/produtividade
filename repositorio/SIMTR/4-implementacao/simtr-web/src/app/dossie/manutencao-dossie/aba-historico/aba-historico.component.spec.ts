import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { AbaHistoricoComponent } from './aba-historico.component';


describe('AbaVinculoComponent', () => {
  let component: AbaHistoricoComponent;
  let fixture: ComponentFixture<AbaHistoricoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AbaHistoricoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AbaHistoricoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
