import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BoxMacroprocessoComponent } from './box-macroprocesso.component';

describe('BoxMacroprocessoComponent', () => {
  let component: BoxMacroprocessoComponent;
  let fixture: ComponentFixture<BoxMacroprocessoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BoxMacroprocessoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BoxMacroprocessoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
