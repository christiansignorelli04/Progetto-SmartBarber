import {
  ApplicationConfig,
  provideAppInitializer,
  inject,
} from "@angular/core";
import { provideHttpClient, withInterceptors } from "@angular/common/http";
import { KeycloakService } from "./keycloak";
import { authInterceptor } from "./auth-interceptor";
export const appConfig: ApplicationConfig = {
  providers: [
    // registriamo l'HttpClient globale e gli passiamo il nostro l'interceptor
    provideHttpClient(withInterceptors([authInterceptor])),

    // eseguiamo l'inizializzazione di Keycloak prima che l'applicazione Angular si avvii
    provideAppInitializer(() => inject(KeycloakService).init()),
  ],
};
