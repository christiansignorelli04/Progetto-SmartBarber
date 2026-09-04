import { Component, OnInit } from "@angular/core";
import { CommonModule, DatePipe } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { KeycloakService } from "./keycloak";
import { BarberApiService } from "./barber-api";
import { Barber, User, Appointment } from "./api.models";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule, DatePipe],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class AppComponent implements OnInit {
  // usiamo delle liste per memorizzare i dati del back-end
  barbers: Barber[] = [];
  appointments: Appointment[] = [];
  userProfile?: User;

  // usiamo delle variabili di stato per gestire l'interfaccia visiva
  activeSection: "prenotazioni" | "profilo" = "prenotazioni";
  messaggio = "";
  errore = "";

  constructor(
    public readonly keycloak: KeycloakService,
    private readonly api: BarberApiService
  ) {}

  // il lifecycle Hook viene eseguito all'avvio del componente
  ngOnInit(): void {
    if (this.keycloak.isLoggedIn()) {
      this.caricaDati();
    }
  }

  // usiamo dei metodi per l'autenticazione
  login(): void {
    this.keycloak.login();
  }

  logout(): void {
    this.keycloak.logout();
  }

  // usiamo un metodo per navigare le schede
  cambiaSezione(sezione: "prenotazioni" | "profilo"): void {
    this.activeSection = sezione;
    this.errore = "";
    this.messaggio = "";
  }

  // usiamo un metodo per caricare tutti i dati se l'utente è loggato
  private caricaDati(): void {
    // 1. Carichiamo il profilo utente
    this.api.getUserProfile().subscribe({
      next: (profilo) => {
        this.userProfile = profilo;
        // se troviamo l'utente carichiamo le sue prenotazioni
        this.caricaPrenotazioni();
      },
      error: () => {
        // se non troviamo il profilo lo mandiamo alla sezione profilo
        this.activeSection = "profilo";
        this.messaggio = "Completa il tuo profilo per iniziare a prenotare.";
      }
    });

    // carichiamo la lista dei barbieri
    this.api.getBarbers().subscribe({
      next: (data) => this.barbers = data,
      error: (err) => console.error("Errore nel caricamento dei barbieri:", err)
    });
  }

  private caricaPrenotazioni(): void {
    this.api.getAppointments().subscribe({
      next: (data) => this.appointments = data,
      error: (err) => console.error("Errore nel caricamento delle prenotazioni:", err)
    });
  }
}
