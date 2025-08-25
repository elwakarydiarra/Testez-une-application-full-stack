import { TestBed } from '@angular/core/testing';
import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should set and get session correctly for a normal user', () => {
    const user: SessionInformation = {
      id: 1,
      firstName: 'Jane',
      lastName: 'Doe',
      admin: false,
      token: 'fake-token-1',
      type: 'user',
      username: 'jane.doe'
    };

    service.logIn(user);

    expect(service.isLogged).toBe(true);

    expect(service.sessionInformation).toEqual(user);

    expect(service.sessionInformation?.admin).toBe(false);
  });

  it('should set and get session correctly for an admin', () => {
    const user: SessionInformation = {
      id: 2,
      firstName: 'John',
      lastName: 'Smith',
      admin: true,
      token: 'fake-token-2',
      type: 'admin',
      username: 'john.smith'
    };

    service.logIn(user);

    expect(service.isLogged).toBe(true);
    expect(service.sessionInformation).toEqual(user);
    expect(service.sessionInformation?.admin).toBe(true);
  });

 it('should log out correctly', () => {
  const user: SessionInformation = {
    id: 3,
    firstName: 'Alice',
    lastName: 'Wonder',
    admin: false,
    token: 'fake-token-3',
    type: 'user',
    username: 'alice.wonder'
  };

  service.logIn(user);
  expect(service.isLogged).toBe(true);

  service.logOut();

  expect(service.isLogged).toBe(false);
  expect(service.sessionInformation).toBeUndefined();
});
});
