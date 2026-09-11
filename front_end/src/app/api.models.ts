/*
 Qui definisco tutte le interfacce che descrivono le strutture dati scambiate con il BackEnd Spring Boot
*/

// il salone
export interface Salon {
  id: number;
  name: string;
  city: string;
  address: string;
  closedDays?: string;
}

// il barbiere
export interface Barber {
  id: number;
  name: string;
  available: boolean;
}

// il trattamento
export interface Treatment {
  id: number;
  name: string;
  price: number;
}

// l'utente
export interface UserRequest {
  email: string;
  firstName: string;
  lastName: string;
  phoneNumber: string;
}

export interface User extends UserRequest {
  id: number;
  keycloakId: string;
  role: string;
}

// la prenotazione
export interface AppointmentRequest {
  userId: number; // il backend lo richiede nel DTO
  barberId: number; // l'operatore scelto
  treatmentId: number; // il servizio scelto
  date: string; // formato anno-mese-giorno
  startTime: string; // formato ore:minuti
}

export interface Appointment {
  id: number;
  date: string;
  startTime: string;
  endTime: string;
  user: User;
  barber: Barber;
  treatment: Treatment;
}
