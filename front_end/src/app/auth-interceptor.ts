import { HttpInterceptorFn } from "@angular/common/http";
import { inject } from "@angular/core";
import { from, switchMap } from "rxjs";
import { KeycloakService } from "./keycloak";

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const keycloak = inject(KeycloakService);

  // convertiamo la Promise di Keycloak, cioè il getToken(), in un Observable
  return from(keycloak.getToken()).pipe(
    switchMap((token) => {
      // se non c'è il token passiamo la richiesta così com'è
      if (!token) {
        return next(req);
      }
      // se c'è il token, cloniamo la richiesta e aggiungiamo l'header "Authorization"
      return next(
        req.clone({
          setHeaders: {
            Authorization: `Bearer ${token}`,
          },
        }),
      );
    }),
  );
};
