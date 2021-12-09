import { TestBed } from '@angular/core/testing';

import { PollService } from './poll-service.service';

describe('PollService', () => {
  let service: PollService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PollService);
  });

});
