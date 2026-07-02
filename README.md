# NovaDoc opdracht

Deze opdracht is een POC voor een vereenvoudigd Document Management Systeem, met Vue 3 web-interface.

## Backend

Start eerst de backend op `http://localhost:8080`. (Zie frontend/src/api/client.ts als de poort veranderd moet worden)

- Installeer de backend-afhankelijkheden volgens de pom.xml.
- Start de server zodat `GET /documents` en `GET /archives?typeId=...` werken.

Hiervoor gebruik ik JetBrains IntelliJ IDEA om de backend te ontwikkelen. Je kan ook de server zelf starten met `mvn spring-boot:run`.

## Frontend

Ga daarna naar de map `frontend`.

```bash
npm install
npm run dev
```

Voor een productiebuild:

```bash
npm run build
```

## Techniek

- Vue 3 + TypeScript
- Vite voor development en build
- PrimeVue voor de interface
- Vue Router voor navigatie
- TanStack Query en Axios voor API-calls
- H2 in-memory database voor de backend & demo doeleinden
