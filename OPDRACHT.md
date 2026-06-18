# Opdracht: Document Management Systeem

**Niveau:** Junior Developer
**Geschatte tijd:** 16–24 uur
**Presentatie:** Demo + code review gesprek

---

## 📋 Inleiding

In deze opdracht bouw je een vereenvoudigd **Document Management Systeem (DMS)**. Dit soort systemen worden in de praktijk veel gebruikt om documenten en mappen gestructureerd op te slaan, te beheren en terug te vinden — denk aan systemen zoals SharePoint, Alfresco of OpenText.

Het doel van deze opdracht is niet alleen een werkend systeem opleveren, maar ook aantonen dat je **begrijpt wat je hebt gebouwd** en **waarom** je bepaalde keuzes hebt gemaakt.

---

## 🎯 Leerdoelen

Na het afronden van deze opdracht heb je aangetoond dat je:

- Een Java backend kunt opzetten met een REST API
- Een frontend kunt koppelen aan een backend
- Werkt met dynamische datastructuren (klasse-definities met metadata)
- Gegevens kunt opslaan en ophalen
- (Optioneel) Event-driven denken begrijpt en kunt toepassen

---

## 🛠️ Technische vereisten

### Backend
- **Java** (minimaal Java 17)
- Framework naar keuze, bijvoorbeeld:
  - Spring Boot *(aanbevolen)*
  - Quarkus
  - Micronaut
- De backend stelt een **REST API** beschikbaar

### Frontend
- Framework naar keuze, bijvoorbeeld:
  - React / Vue / Angular
  - Thymeleaf (server-side, gekoppeld aan Spring Boot)
  - Een eenvoudige HTML/JS applicatie
- De frontend communiceert met de backend via de REST API

### Opslag
- De manier van opslaan mag je zelf kiezen:
  - Relationele database (bijv. PostgreSQL, MySQL, H2)
  - NoSQL database (bijv. MongoDB)
  - Bestandssysteem
  - In-memory opslag (voor prototype/demo doeleinden)

> 💡 **Tip:** Documenteer je keuze en leg uit waarom je hiervoor hebt gekozen.

---

## 📌 Functionele eisen

### 1. Definiëren van klassen

Via de frontend moet het mogelijk zijn om **document- en mapklassen** te definiëren.

Een klasse heeft:
- Een **naam** (bijv. "Factuur", "Contract", "Projectmap")
- Een of meerdere **metadata-velden**, elk met:
  - Een veldnaam (bijv. "Factuurnummer", "Vervaldatum")
  - Een **datatype**, keuze uit:
    - `String`
    - `Integer`
    - `Boolean`
    - `Datum`

**Voorbeelden van klassen:**

| Klassenaam  | Type     | Metadata-velden                                                    |
|-------------|----------|--------------------------------------------------------------------|
| Factuur     | Document | Factuurnummer (String), Bedrag (Integer), Betaald (Boolean), Vervaldatum (Datum) |
| Contract    | Document | Contractnummer (String), Startdatum (Datum), Actief (Boolean)     |
| Projectmap  | Map      | Projectnaam (String), Projectnummer (Integer), Startdatum (Datum) |

---

### 2. Aanmaken van documenten en mappen

Via de frontend moet het mogelijk zijn om **een document of map aan te maken** in het systeem:

- De gebruiker kiest een **bestaande klasse** (indien er meerdere zijn gedefinieerd)
- Op basis van de gekozen klasse worden de bijbehorende **metadata-velden getoond**
- De gebruiker vult de waarden in voor elk veld
- Na bevestiging wordt het document of de map **opgeslagen in het systeem**

**Validatie-eisen:**
- Verplichte velden mogen niet leeg zijn
- Datatypen moeten correct worden gevalideerd (bijv. een datum-veld mag geen tekst bevatten)

---

### 3. Overzicht

Via de frontend is een **overzicht** beschikbaar van:
- Alle gedefinieerde klassen
- Alle aangemaakte documenten en mappen, inclusief hun metadata-waarden

---

### 4. ⭐ Extra: Events bij aanmaken (optioneel)

Wanneer een document of map wordt aangemaakt, kan er een **event worden getriggerd**.

Implementeer minimaal **één** van de volgende opties:

| Optie | Beschrijving |
|-------|--------------|
| **E-mail notificatie** | Er wordt een e-mail verstuurd met de gegevens van het nieuwe document/map |
| **REST API aanroep** | Er wordt een externe REST API aangeroepen (bijv. een webhook) |
| **Logging event** | Er wordt een gedetailleerd event weggeschreven naar een logbestand of logsysteem |
| **Eigen voorstel** | Je mag ook een eigen event-mechanisme voorstellen, mits je dit kunt verantwoorden |

> 💡 **Denk na over:** Hoe zorg je ervoor dat het systeem uitbreidbaar is? Wat zou een goed design pattern zijn voor events?

---

## 🗂️ Inlevering

Lever het volgende in:

1. **Broncode** — via een Git repository (bijv. GitHub, GitLab, Bitbucket)
   - Zorg voor een duidelijke mapstructuur
   - Voeg een `.gitignore` toe
2. **README.md** in de repository met:
   - Beschrijving van de applicatie
   - Instructies om de applicatie lokaal op te starten
   - Uitleg van de technische keuzes die je hebt gemaakt
   - (Optioneel) Architectuuroverzicht of diagram
3. **Werkende demo** — de applicatie moet lokaal draaibaar zijn tijdens de presentatie

---

## 🎤 Presentatie & Demo

De presentatie bestaat uit twee delen:

### Deel 1 — Demo (±10 minuten)
Demonstreer live de volgende handelingen:
1. Definieer een nieuwe **documentklasse** met minimaal 3 metadata-velden van verschillende typen
2. Definieer een nieuwe **mapklasse**
3. Maak een **document aan** op basis van de gedefinieerde klasse
4. Maak een **map aan** op basis van de gedefinieerde klasse
5. Laat het **overzicht** zien van alle documenten en mappen
6. *(Bij extra opdracht)* Demonstreer het event dat wordt getriggerd

### Deel 2 — Code review gesprek (±15 minuten)
Je krijgt vragen over je eigen code. Voorbeeldvragen:

- *"Leg uit hoe de REST API-communicatie tussen frontend en backend werkt."*
- *"Hoe heb je de metadata-velden dynamisch opgeslagen? Waarom heb je hiervoor gekozen?"*
- *"Wat zou er veranderen als je een ander opslagmechanisme zou kiezen?"*
- *"Hoe valideer je de invoer van de gebruiker?"*
- *"Wat zijn de beperkingen van je huidige oplossing?"*
- *(Bij extra opdracht)* *"Hoe zorg je ervoor dat er eenvoudig een tweede event-type aan toegevoegd kan worden?"*

> ⚠️ **Let op:** Je moet kunnen uitleggen wat elke regel code doet. Code die je niet begrijpt, telt niet mee.

---

## ✅ Beoordelingscriteria

| Criterium                                        | Gewicht |
|--------------------------------------------------|---------|
| Werkende applicatie (backend + frontend)         | 25%     |
| Correct implementeren van klasse-definities      | 20%     |
| Correct aanmaken van documenten en mappen        | 20%     |
| Codekwaliteit en structuur                       | 15%     |
| Begrip tijdens code review gesprek               | 15%     |
| Extra: Event-mechanisme                          | +10%    |
| Extra: README / documentatie / Git gebruik       | +5%     |

---

## 💡 Tips voor de student

- Begin klein: zorg eerst dat de basis werkt voordat je extra's toevoegt
- Gebruik Git actief tijdens het ontwikkelen — commit regelmatig met duidelijke berichten
- Test je REST API met een tool zoals **Postman** of **Swagger UI**
- Denk na over foutafhandeling: wat gebeurt er als een gebruiker ongeldige invoer geeft?
- Vraag jezelf af: *"Als een collega deze code overneemt, begrijpt die dan wat er staat?"*
- Voor de extra opdracht: zoek op **Observer pattern** of **Spring Application Events**

---

## 📚 Mogelijk nuttige bronnen

- [Spring Boot officiële documentatie](https://spring.io/projects/spring-boot)
- [Building a RESTful Web Service – Spring Guide](https://spring.io/guides/gs/rest-service/)
- [Jackson – JSON in Java](https://github.com/FasterXML/jackson)
- [H2 in-memory database](https://www.h2database.com/)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)

---

*Succes met de opdracht! Het gaat er niet om dat alles perfect is, maar dat je kunt laten zien dat je begrijpt wat je hebt gebouwd en hoe je problemen aanpakt.*