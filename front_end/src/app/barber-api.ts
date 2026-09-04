import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import {
  Barber,
  User,
  UserRequest,
  Appointment,
  AppointmentRequest,
} from "./api.models";

@Injectable({ providedIn: "root" })
export class BarberApiService {

  // URL base per le chiamate API, tramite il proxy.conf.json verrà inoltrato a "http://localhost:8080/api"
  private readonly baseUrl = "/api";

  constructor(private readonly http: HttpClient) {}

  // servizi barbiere

  getBarbers(): Observable<Barber[]> {
    return this.http.get<Barber[]>(`${this.baseUrl}/barbers`);
  }

  // servizi utente

  getUserProfile(): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/users/me`);
  }

  saveUserProfile(request: UserRequest): Observable<User> {
    return this.http.post<User>(`${this.baseUrl}/users/me`, request);
  }

  // servizi prenotazioni

  getAppointments(): Observable<Appointment[]> {
    return this.http.get<Appointment[]>(`${this.baseUrl}/appointments`);
  }

  createAppointment(request: AppointmentRequest): Observable<Appointment> {
    return this.http.post<Appointment>(
      `${this.baseUrl}/appointments`,
      request
    );
  }

  deleteAppointment(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/appointments/${id}`);
  }
}
