# Arcano - backend per l'organizzazione e la gestione di eventi sportivi
### Riccardo Malavolti

### Introduzione
Enumerare tutte le tipologie e varietà di giochi di carte è un'impresa destinata ad occupare molto tempo: oltre ai "classici", diffusi e conosciuti dalla quasi totalità delle persone, negli ultimi decenni hanno spopolato nuove tipologie di carte, che uniscono la componente tradizionale del gioco con quella del collezionismo. I giochi di carte collezionabili (GCC) hanno riscosso un enorme successo, arrivando a organizzare tornei con migliaia di partecipanti con montepremi multimilionari. 
Con l'elaborato presentato si vuole provare a fornire una soluzione off-line per l'organizzazione e la gestione di questi tornei, adatta sia a piccoli ritrovi che a grandi manifestazioni. Arcano è il primo tassello di questa soluzione, un backend scritto in Java che dovrà essere sfruttato dall'applicazione Congrega, in dotazione ai giocatori e organizzatori del torneo.
Questa relazione illustra innanzitutto i requisiti funzionali dettati dal dominio di applicazione, i casi d'uso e la creazione del domain model. 


## Una soluzione collaborativa

Le community di gioco sono abbastanza efficienti nell'organizzare tornei, che si tratti di un gruppo di amici o di un palazzetto dello sport stracolmo di partecipanti gli organizzatori riescono quasi sempre a gestire iscrizioni, fasi, gironi e punteggi. Nel caso di *Magic: The Gathering* la società che possiede il marchio organizza in prima persona i tornei con i montepremi più ricchi, ma permette a privati accreditati (perlopiù negozi o associazioni di medie dimensioni) di organizzare eventi sanzionati concedendo un software per la registrazione dei punti e la gestione dell'evento, purché sia presente un arbitro accreditato. Questo software però è obsoleto, difficile da configurare e per nulla *user-friendly*, ma la vera mancanza è quella di non poter garantire un accesso ai dati per giocatori presenti ed iscritti. L'unico modo di avere uno sguardo sull'andamento dell torneo è quello di controllare le graduatorie alla fine di ogni girone, che vengono stampate e affisse.

**Arcano** nasce con l'obiettivo di restituire un po' trasparenza nei riguardi dei giocatori: offrendo sia una piattaforma per la gestione dell'evento, sia un punto di accesso per la consultazione di statistiche e informazioni in tempo reale ai partecipanti della manifestazione.

### 1.1 Requisiti e casi d'uso
I requisiti funzionali del servizio provengono da più attori:

- **Giocatori**: sono coloro che effettivamente partecipano all'evento. Sono responsabili del loro account (contenente le loro informazioni personali) e del'aggiornamento dello stato di ogni match che li riguarda. Possono convocare un Giudice per risolvere diatribe o chiedere chiarimenti. 
- **Giudici**: regolano e sanzionano l'evento, risolvendo i conflitti tra giocatori quando convocati. Possono assegnare punteggi o decretare lo stato un di match o di un gioco.
- **Amministratori**: organizzano e monitorano l'evento, creando le fasi e i gironi. Possono espellere i giocatori e applicare le sanzioni imposte dai giudici. 

Lo schema in figura [1.1] riassume i casi d'uso derivati dai requisiti sopraelencati.

![](UseCaseDiagram.svg)

### 1.2  Modello di dominio
Con la definizione dei rouli si è passati alla progettazione del modello di dominio, la figura [1.2] schematizza le relazioni tra le entità coinvolte.

![](class_diagram.png)

[Figura 1.2]


- **User**: riassume e generalizza Giocatori, Giudici e Amministratori. I ruoli vengono identificati tramite il campo `ROLE`, che fa riferimento al rispettivo enumerable.

- **Event**: identifica un torneo, è composto da:
    - Una lista di `Match`, ovvero tutti gli incontri che compongono l'evento.
    - Tre liste di `User`:
        - `adminList`, gli utenti che hanno privilegi di amministratore su quell'evento, possono creare gli incontri e modificare i punteggi.
        - `judgeList`, utenti Giudice che ricevono le notifiche di convocazione.
        - `playerList`, utenti che partecipano all'evento in qualità di giocatori.
    Un Evento è `Ownable` da tutti gli amministratori in `adminList`.

- **Match**: rappresenta un incontro tra due giocatori, è composto da:
    - Un set di `Game`, ovvero gli scontri. Tipicamente sono tre.
    - Due riferimenti a `User`, ovvero i due giocatori coinvolti.
    - 


### 1.3  Design delle API

## Implementazione del backend

### 2.1 Implementazione degli endpoint
#### Architettura REST vs GraphQL

### 2.2 Architettura N-layered

#### Service Layer
#### Repository Layer
#### Persistence Layer

#### Autenticazione e Autorizzazione

## Deployment
### 3.1 WildFly 20
### 3.2 Docker-Compose: orchestrazione dei servizi

