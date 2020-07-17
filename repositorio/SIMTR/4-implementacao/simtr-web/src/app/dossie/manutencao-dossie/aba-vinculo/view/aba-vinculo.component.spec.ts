import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { AbaVinculoComponent } from './aba-vinculo.component';


describe('AbaVinculoComponent', () => {
  let component: AbaVinculoComponent;
  let fixture: ComponentFixture<AbaVinculoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AbaVinculoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AbaVinculoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
