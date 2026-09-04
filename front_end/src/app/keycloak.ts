import { Injectable } from "@angular/core";
import Keycloak from "keycloak-js";

@Injectable({ providedIn: "root" })
export class KeycloakService {
  private readonly keycloak: Keycloak = new Keycloak({
    url: "http://localhost:8081",
    realm: "smartbarber",
    clientId: "smartbarber-client",
  });

  async init(): Promise<void> {
    await this.keycloak.init({
      onLoad: "check-sso",
      pkceMethod: "S256",
      checkLoginIframe: false,
    });
  }

  isLoggedIn(): boolean {
    return !!this.keycloak.authenticated;
  }

  login(): Promise<void> {
    return this.keycloak.login();
  }

  register(): Promise<void> {
    return this.keycloak.register();
  }

  logout(): Promise<void> {
    return this.keycloak.logout({ redirectUri: window.location.origin });
  }

  async getToken(): Promise<string | undefined> {
    if (!this.isLoggedIn()) {
      return undefined;
    }
    await this.keycloak.updateToken(30);
    return this.keycloak.token;
  }

  username(): string {
    return (this.keycloak.tokenParsed?.["preferred_username"] as string) || "";
  }

  email(): string {
    return (this.keycloak.tokenParsed?.["email"] as string) || "";
  }
}
