import { Component, OnInit, ChangeDetectorRef } from "@angular/core";
import { CommonModule, TitleCasePipe } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { KeycloakService } from "./keycloak";
import { BarberApiService } from "./barber-api";
import { Salon, Barber, Treatment, User, Appointment } from "./api.models";
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule, TitleCasePipe, FormsModule, MatToolbarModule, MatButtonModule,
    MatIconModule, MatCardModule, MatListModule, MatFormFieldModule, MatInputModule,
    MatSelectModule
  ],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class AppComponent implements OnInit {
  salons: Salon[] = [];
  appuntamentiFuturi: Appointment[] = [];
  appuntamentiPassati: Appointment[] = [];
  userProfile?: User;

  activeSection: "prenotazioni" | "profilo" | "gestore" = "prenotazioni";
  messaggio = ""; errore = ""; totaleAppuntamenti: number = 0;
  isEditingProfile = false;

  profiloForm = { firstName: "", lastName: "", email: "", phoneNumber: "" };

  newSalonName = ""; newSalonCity = ""; newSalonAddress = "";
  newSalonClosedDays: string[] = []; // <-- FASE 4: Giorni chiusi
  cittaRicercata = ""; ricercaEffettuata = false;
  faseAttuale: "ricerca" | "prenotazione" = "ricerca";
  saloneSelezionato?: Salon;

  trattamentiSalone: Treatment[] = []; operatoriSalone: Barber[] = [];
  trattamentoSceltoId?: number; operatoreSceltoId?: number;
  dataScelta: string = ""; oraScelta: string = "";
  oggi: string = ""; // <-- FASE 4: Data odierna

  orariDisponibili: string[] = [
    "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00",
    "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00"
  ];
  orariOccupati: string[] = [];

  mostraPopupCancellazione = false; appuntamentoDaCancellareId?: number;

  newBarberName = ""; newTreatmentName = ""; newTreatmentPrice?: number;
  appuntamentiGestore: Appointment[] = [];

  constructor(public readonly keycloak: KeycloakService, private readonly api: BarberApiService, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    // calcolo la data odierna nel formato anno-mese-giorno considerando la fuso orario locale
    const d = new Date();
    d.setMinutes(d.getMinutes() - d.getTimezoneOffset());
    this.oggi = d.toISOString().split('T')[0];

    if (this.keycloak.isLoggedIn()) { this.caricaDati(); }
  }

  login(): void { this.keycloak.login(); }
  logout(): void { this.keycloak.logout(); }

  cambiaSezione(sezione: "prenotazioni" | "profilo" | "gestore"): void {
    this.activeSection = sezione; this.errore = ""; this.messaggio = ""; this.tornaAllaRicerca();
    this.isEditingProfile = false;
    if (sezione === "gestore") {
      this.api.getAppointmentsCount().subscribe({
        next: (numero) => { this.totaleAppuntamenti = numero; this.cdr.detectChanges(); },
        error: (err) => { console.error(err); this.cdr.detectChanges(); }
      });
      this.caricaCalendarioGestore();
    }
    this.cdr.detectChanges();
  }

  private caricaDati(): void {
    this.api.getUserProfile().subscribe({
      next: (profilo) => { this.userProfile = profilo; this.caricaPrenotazioni(); this.cdr.detectChanges(); },
      error: () => { this.messaggio = "Completa il tuo profilo prima di prenotare."; this.cdr.detectChanges(); }
    });
  }

  attivaModificaProfilo(): void {
    if (this.userProfile) {
      this.profiloForm = { firstName: this.userProfile.firstName, lastName: this.userProfile.lastName, email: this.userProfile.email, phoneNumber: this.userProfile.phoneNumber || "" };
      this.isEditingProfile = true;
    }
  }

  annullaModificaProfilo(): void { this.isEditingProfile = false; this.errore = ""; }

  salvaProfilo(): void {
    if (!this.profiloForm.firstName || !this.profiloForm.lastName || !this.profiloForm.email) {
      this.errore = "Compila almeno Nome, Cognome ed Email."; return;
    }
    this.api.saveUserProfile(this.profiloForm).subscribe({
      next: (profilo) => {
        this.userProfile = profilo; this.messaggio = "Profilo salvato!"; this.errore = ""; this.isEditingProfile = false; this.cdr.detectChanges();
      },
      error: () => { this.errore = "Errore durante il salvataggio."; this.cdr.detectChanges(); }
    });
  }

  private caricaPrenotazioni(): void {
    this.api.getAppointments().subscribe({
      next: (data) => {
        const adesso = new Date();
        this.appuntamentiFuturi = []; this.appuntamentiPassati = [];
        data.forEach(app => {
          const dataApp = new Date(`${app.date}T${app.startTime}`);
          if (dataApp >= adesso) { this.appuntamentiFuturi.push(app); } else { this.appuntamentiPassati.push(app); }
        });
        this.cdr.detectChanges();
      },
      error: (err) => { console.error(err); this.cdr.detectChanges(); }
    });
  }

  apriPopupCancellazione(id: number): void { this.appuntamentoDaCancellareId = id; this.mostraPopupCancellazione = true; this.cdr.detectChanges(); }
  chiudiPopupCancellazione(): void { this.mostraPopupCancellazione = false; this.appuntamentoDaCancellareId = undefined; this.cdr.detectChanges(); }

  confermaCancellazione(): void {
    if (this.appuntamentoDaCancellareId) {
      this.api.deleteAppointment(this.appuntamentoDaCancellareId).subscribe({
        next: () => { this.messaggio = "Annullato!"; this.errore = ""; this.chiudiPopupCancellazione(); this.caricaPrenotazioni(); },
        error: (err) => { this.errore = err.error?.message || "Impossibile cancellare."; this.chiudiPopupCancellazione(); }
      });
    }
  }

  avviaRicerca(): void {
    if (!this.cittaRicercata || this.cittaRicercata.trim() === '') { this.ricercaEffettuata = false; this.salons = []; this.cdr.detectChanges(); return; }
    this.ricercaEffettuata = true;
    this.api.getSalons(this.cittaRicercata.trim().toLowerCase()).subscribe({
      next: (data) => { this.salons = data; this.cdr.detectChanges(); },
      error: (err) => { console.error(err); this.cdr.detectChanges(); }
    });
  }

  onSearchChange(): void {
    if (!this.cittaRicercata || this.cittaRicercata.trim() === '') { this.ricercaEffettuata = false; this.salons = []; this.cdr.detectChanges(); }
  }

  apriSalone(salon: Salon): void {
    this.saloneSelezionato = salon; this.faseAttuale = "prenotazione";
    this.trattamentoSceltoId = undefined; this.operatoreSceltoId = undefined; this.dataScelta = ""; this.oraScelta = "";
    this.orariOccupati = [];
    this.api.getTreatmentsBySalon(salon.id).subscribe(data => { this.trattamentiSalone = data; this.cdr.detectChanges(); });
    this.api.getBarbersBySalon(salon.id).subscribe(data => { this.operatoriSalone = data; this.cdr.detectChanges(); });
  }

  aggiornaDisponibilita(): void {
    this.orariOccupati = [];
    this.oraScelta = "";
    this.errore = "";

    if (this.operatoreSceltoId && this.dataScelta) {
      const selectedDate = new Date(this.dataScelta);
      // controllo le festività
      const monthDay = this.dataScelta.substring(5); // Estrae MM-DD
      const holidays = ["01-01", "01-06", "04-25", "05-01", "06-02", "08-15", "11-01", "12-08", "12-25", "12-26"];
      if (holidays.includes(monthDay)) {
        this.errore = "Il salone è chiuso in questa data per festività (Giorno Rosso).";
        this.orariOccupati = [...this.orariDisponibili];
        return;
      }

      // controllo le chiusure settimanali se il salone le ha impostate
      const dayOfWeek = selectedDate.getDay().toString();
      if (this.saloneSelezionato?.closedDays?.includes(dayOfWeek)) {
        this.errore = "Il salone osserva il suo turno di chiusura in questo giorno della settimana.";
        this.orariOccupati = [...this.orariDisponibili];
        return;
      }

      // verifico gli orari occupati e gli orari passati di oggi
      this.api.getBookedTimes(this.operatoreSceltoId, this.dataScelta).subscribe({
        next: (times) => {
          this.orariOccupati = times.map(t => t.substring(0, 5));

          if (this.dataScelta === this.oggi) {
            const nowHour = new Date().getHours();
            const nowMin = new Date().getMinutes();
            this.orariDisponibili.forEach(ora => {
              const [h, m] = ora.split(':').map(Number);
              if (h < nowHour || (h === nowHour && m <= nowMin)) {
                this.orariOccupati.push(ora);
              }
            });
          }
          this.cdr.detectChanges();
        },
        error: (err) => console.error("Errore disponibilità:", err)
      });
    }
  }

  tornaAllaRicerca(): void { this.faseAttuale = "ricerca"; this.saloneSelezionato = undefined; this.messaggio = ""; this.errore = ""; this.cdr.detectChanges(); }

  confermaPrenotazione(): void {
    if (!this.userProfile) { this.errore = "Profilo non completato."; return; }
    if (!this.trattamentoSceltoId || !this.operatoreSceltoId || !this.dataScelta || !this.oraScelta) { this.errore = "Compila tutte le selezioni."; return; }
    const request = { userId: this.userProfile.id, barberId: this.operatoreSceltoId, treatmentId: this.trattamentoSceltoId, date: this.dataScelta, startTime: this.oraScelta + ":00" };
    this.api.createAppointment(request).subscribe({
      next: () => { this.messaggio = "Prenotazione confermata!"; this.errore = ""; this.caricaPrenotazioni(); this.tornaAllaRicerca(); this.cdr.detectChanges(); },
      error: (err) => { this.errore = err.error?.message || "Errore."; this.cdr.detectChanges(); }
    });
  }

  aggiungiSalone(): void {
    if (!this.newSalonName || !this.newSalonCity || !this.newSalonAddress) { this.errore = "Compila tutti i campi!"; return; }
    const closedDaysStr = this.newSalonClosedDays.join(',');
    this.api.createSalon(this.newSalonName, this.newSalonCity, this.newSalonAddress, closedDaysStr).subscribe({
      next: () => {
        this.messaggio = "Salone creato!";
        this.newSalonName = ""; this.newSalonCity = ""; this.newSalonAddress = ""; this.newSalonClosedDays = [];
        this.cdr.detectChanges();
      },
      error: (err) => { this.errore = err.error?.message || "Errore."; this.cdr.detectChanges(); }
    });
  }

  aggiungiBarbiereGestore(): void {
    if (!this.newBarberName) { this.errore = "Inserisci il nome."; return; }
    this.api.addBarber(this.newBarberName).subscribe({
      next: () => { this.messaggio = "Barbiere aggiunto!"; this.newBarberName = ""; this.cdr.detectChanges(); },
      error: (err) => { this.errore = err.error?.message || "Errore."; this.cdr.detectChanges(); }
    });
  }

  aggiungiTrattamentoGestore(): void {
    if (!this.newTreatmentName || !this.newTreatmentPrice) { this.errore = "Inserisci nome e prezzo."; return; }
    this.api.addTreatment(this.newTreatmentName, this.newTreatmentPrice).subscribe({
      next: () => { this.messaggio = "Trattamento aggiunto!"; this.newTreatmentName = ""; this.newTreatmentPrice = undefined; this.cdr.detectChanges(); },
      error: (err) => { this.errore = err.error?.message || "Errore."; this.cdr.detectChanges(); }
    });
  }

  caricaCalendarioGestore(): void {
    this.api.getOwnerAppointments().subscribe({
      next: (data) => { this.appuntamentiGestore = data; this.cdr.detectChanges(); },
      error: (err) => { console.error(err); this.cdr.detectChanges(); }
    });
  }
}
