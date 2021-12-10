import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GrowthBookComponent } from './growth-book.component';

describe('GrowthBookComponent', () => {
  let component: GrowthBookComponent;
  let fixture: ComponentFixture<GrowthBookComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GrowthBookComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GrowthBookComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
