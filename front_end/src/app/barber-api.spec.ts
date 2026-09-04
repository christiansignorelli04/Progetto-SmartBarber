import { TestBed } from '@angular/core/testing';
import { BarberApiService } from './barber-api';

describe('BarberApiService', () => {
  let service: BarberApiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BarberApiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
