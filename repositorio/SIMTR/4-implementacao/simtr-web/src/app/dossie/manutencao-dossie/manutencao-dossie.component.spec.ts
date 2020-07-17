import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ManutencaoDossieComponent } from './manutencao-dossie.component';

describe('ManutencaoDossieComponent', () => {
  let component: ManutencaoDossieComponent;
  let fixture: ComponentFixture<ManutencaoDossieComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ManutencaoDossieComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ManutencaoDossieComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
