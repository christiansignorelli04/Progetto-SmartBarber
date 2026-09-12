import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Salon, Barber, Treatment, User, UserRequest, Appointment, AppointmentRequest } from "./api.models";

@Injectable({ providedIn: "root" })
export class BarberApiService {

  private readonly baseUrl = "/api";

  constructor(private readonly http: HttpClient) {}

  getSalons(city: string = ""): Observable<Salon[]> {
    let params = new HttpParams();
    if (city && city.trim() !== "") { params = params.set("city", city.trim()); }
    return this.http.get<Salon[]>(`${this.baseUrl}/salons`, { params });
  }

  createSalon(name: string, city: string, address: string, closedDays: string): Observable<Salon> {
    let params = new HttpParams()
      .set("name", name).set("city", city).set("address", address);

    if (closedDays) { params = params.set("closedDays", closedDays); } // invio i giorni scelti

    return this.http.post<Salon>(`${this.baseUrl}/salons`, null, { params });
  }

  deleteBarber(barberId: number) {
    // Sostituisci '/api' con l'URL base corretto se nel tuo servizio è definito diversamente (es. this.baseUrl)
    return this.http.delete(`/api/barbers/${barberId}`);
  }

  getBarbersBySalon(salonId: number): Observable<Barber[]> {
    return this.http.get<Barber[]>(`${this.baseUrl}/salons/${salonId}/barbers`);
  }

  getTreatmentsBySalon(salonId: number): Observable<Treatment[]> {
    return this.http.get<Treatment[]>(`${this.baseUrl}/salons/${salonId}/treatments`);
  }

  getUserProfile(): Observable<User> { return this.http.get<User>(`${this.baseUrl}/users/me`); }

  saveUserProfile(request: UserRequest): Observable<User> {
    return this.http.post<User>(`${this.baseUrl}/users/me`, request);
  }

  // recupero solo le prenotazioni fatte come cliente
  getAppointments(): Observable<Appointment[]> {
    return this.http.get<Appointment[]>(`${this.baseUrl}/appointments/my-bookings`);
  }

  createAppointment(request: AppointmentRequest): Observable<Appointment> {
    return this.http.post<Appointment>(`${this.baseUrl}/appointments`, request);
  }

  deleteAppointment(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/appointments/${id}`);
  }

  getAppointmentsCount(): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/appointments/count`);
  }

  addBarber(name: string) {
    const params = new HttpParams().set('name', name);
    return this.http.post<Barber>(`${this.baseUrl}/barbers`, null, { params });
  }

  addTreatment(name: string, price: number) {
    const params = new HttpParams().set('name', name).set('price', price.toString());
    return this.http.post<Treatment>(`${this.baseUrl}/treatments`, null, { params });
  }

  getOwnerAppointments() {
    return this.http.get<Appointment[]>(`${this.baseUrl}/appointments/my-salon`);
  }

  // chiedo al server quali orari nascondere
  getBookedTimes(barberId: number, date: string) {
    const params = new HttpParams().set('barberId', barberId.toString()).set('date', date);
    return this.http.get<string[]>(`${this.baseUrl}/appointments/booked-times`, { params });
  }

  getMyBarbers() {
    return this.http.get<Barber[]>('/api/barbers/my-barbers');
  }
}
