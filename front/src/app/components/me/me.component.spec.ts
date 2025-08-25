import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { of } from 'rxjs';

import { SessionService } from 'src/app/services/session.service';
import { UserService } from 'src/app/services/user.service';
import { MeComponent } from './me.component';
import { User } from 'src/app/interfaces/user.interface';


import { expect, jest } from '@jest/globals';
import { OverlayModule } from '@angular/cdk/overlay';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { RouterTestingModule } from '@angular/router/testing';



describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let userService: jest.Mocked<UserService>;
  let sessionService: any;
  let router: Router;
  let snackBar: MatSnackBar;

  const mockUser: User = {
    id: 1,
    email: 'test@test.com',
    firstName: 'John',
    lastName: 'Doe',
    password: 'pass',
    admin: true,
    createdAt: new Date()
  };

  beforeEach(async () => {
    sessionService = {
      sessionInformation: { id: 1, admin: true },
      logOut: jest.fn()
    };

    const userServiceMock = {
      getById: jest.fn().mockReturnValue(of(mockUser)),
      delete: jest.fn().mockReturnValue(of({}))
    } as unknown as jest.Mocked<UserService>;

    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        RouterTestingModule,
        MatSnackBarModule,
        OverlayModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        { provide: SessionService, useValue: sessionService },
        { provide: UserService, useValue: userServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    userService = TestBed.inject(UserService) as jest.Mocked<UserService>;
    router = TestBed.inject(Router);
    snackBar = TestBed.inject(MatSnackBar);

    jest.spyOn(router, 'navigate').mockResolvedValue(true);
    jest.spyOn(snackBar, 'open');

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load user on init', () => {
    expect(userService.getById).toHaveBeenCalledWith(sessionService.sessionInformation.id.toString());
    expect(component.user).toEqual(mockUser);
  });

  it('should go back when back() is called', () => {
    const spy = jest.spyOn(window.history, 'back');
    component.back();
    expect(spy).toHaveBeenCalled();
  });

  it('should delete account and logout', fakeAsync(() => {

    const injectedSessionService = TestBed.inject(SessionService) as any;
    const logoutSpy = jest.spyOn(injectedSessionService, 'logOut');


    jest.spyOn(snackBar, 'open').mockReturnValue({} as any);

    component.delete();


    tick();

    expect(userService.delete)
      .toHaveBeenCalledWith(sessionService.sessionInformation.id.toString());
    expect(snackBar.open)
      .toHaveBeenCalledWith('Your account has been deleted !', 'Close', { duration: 3000 });
    expect(logoutSpy).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/']);
  }));
});
