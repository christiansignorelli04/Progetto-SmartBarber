/*
 Qui definisco tutte le interfacce che descrivono le strutture dati scambiate con il BackEnd Spring Boot.
 Le interfacce TypeScript non creano oggetti durante l'esecuzione, ma servono solo al
 compilatore per controllare che i tipi e i campi siano corretti.
*/

// barbiere
export interface Barber {
  id: number;
  name: string;
  available: boolean;
}

// utente
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

// prenotazione
export interface AppointmentRequest {
  barberId: number;
  date: string; // Formato anno-mese-giorno
  startTime: string; // Formato anno-mese-giornoTora:minuti:secondi
  endTime: string;   // Formato anno-mese-giornoTora:minuti:secondi
}

export interface Appointment {
  id: number;
  date: string;
  startTime: string;
  endTime: string;

  // altre informazioni che arriveranno dal backend
  userId: number;
  userFirstName: string;
  userLastName: string;

  barberId: number;
  barberName: string;
}
