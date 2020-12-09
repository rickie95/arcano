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

##### Figura [1.1]: Casi d'uso
![](UseCaseDiagram.svg)

### 1.2  Modello di dominio
Con la definizione dei rouli si è passati alla progettazione del modello di dominio, la figura [1.2] schematizza le relazioni tra le entità coinvolte.


##### Figura [1.2]: Schema del domain model. 
![](domain_model.svg)

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
    - Un riferimento a `Event`, che identifica l'evento a cui afferisce il match.
- **Game**: ovvero la suddivisione dell'incontro. Oltre ad un identificativo e un booleano per lo status è composto anche da:
    - Una mappa <Long, Short> che lega l'identificativo del giocatore e il suo punteggio.


### 1.3  Design delle API
Stabilito il modello e le interazioni con il sistema da parte degli attori è possibile stabilire l'interfaccia da esporre. Si è deciso quindi per rendere disponibili i seguenti endpoint:

- `/users` 
- `/events`
- `/matches`
- `/games`

Sono stati inclusi anche un endpoint per l'autenticazione, uno dedicato all'uso della libreria GraphQL e una websocket per la comunicazione in tempo reale dei risultati di gioco. I dati vengono scambiati esclusivamente in formato JSON; si veda la tabella seguente [1.3] per una panoramica delle API esposte.

##### Tabella [1.3]: API disponibili
| URL           | Verbo  | Funzionalità |
|---------------|--------|--------------|
| **Utenti**    | | |
| `/users`      | GET    | Restituisce una lista con tutti gli utenti iscritti |
| `/users`      | POST   | Aggiunge un nuovo utente al sistema |
| `/users/{id}` | GET    | Restituisce una visione dettagliata dell'utente selezionato |
| `/users/{id}` | PUT    | Aggiorna le informazioni dell'utente selezionato |
| `/users/{id}` | DELETE | Rimuove l'utente selezionato|
| **Eventi**    | | |
| `/events`     | GET    | Restituisce una lista con la descrizione sommaria di tutti gli eventi |
| `/events`     | POST   | Crea un nuovo evento |
| `/events/{id}`| GET    | Restituisce una versione dettagliata dell'evento |
| `/events/{id}`| PUT    | Aggiorna l'evento selezionato |
| `/events/{id}`| DELETE | Cancella l'evento selezionato |
| `/events/{id}/players`| GET  | Restituisce una lista con i giocatori partecipanti all'evento |
| `/events/{id}/players`| POST | Iscrive un giocatore all'evento |
| `/events/{id}/players`| DELETE  | Rimuove un giocatore dall'evento |
| `/events/{id}/judges`| GET  | Restituisce una lista con i giudici partecipanti all'evento |
| `/events/{id}/judges`| POST  | Iscrive un utente all'evento come giudice |
| **Match**     | | |
| `/matches`| GET  | Restituisce una lista sommaria degli incontri |
| `/matches`| POST  | Crea un nuovo incontro |
| `/matches/{id}`| PUT  | Aggiorna l'incontro selezionato |
| `/matches/{id}`| DELETE  | Rimuove l'incontro selezionato |
| `/matches/ofEvent/{id}`| GET  | Restituisce una lista sommaria degli incontri afferenti all'evento con id={id}|
| **Game**      | | | 
| `/games`      | POST | Crea un nuovo gioco |
| `/games/{id}` | GET | Restituisce informazioni dettagliate per il gioco selezionato | 

#### WebSocket
La websocket è disponibile all'URL `/ws-arcano/{gameId}/{playerId}`.

## Implementazione del backend
Il servizio è stato realizzato sfruttando lo stack JEE:

- **JAX-RS** per gli endpoint REST
- **CDI** per automatizzare la dependency injection nelle varie risorse.
- **JPA** per la connessione e la persistenza degli oggetti in un database.

### 2.1 Implementazione degli endpoint
Per la realizzazione degli endpoint descritti nelle sezioni precedenti si è evitato l'uso di annotazioni specifiche di un'implementazione di JAX-RS, in modo da assicurare una piena compatibilità con qualsiasi application server.

Creare ed esporre una risorsa è piuttosto immediato, è sufficiente indicare l'URL relativo con l'annotazione `@Path` e aggiungere qualche metodo associato ad un verbo HTTP tramite le annotazioni dedicate. L'esempio seguente mostra una possibile implementazione:

    @Path("events")
    public class EventEndpoint {

        @GET
	    @Produces(MediaType.APPLICATION_JSON)
	    public List<Event> getAllEvents(){
		    return eventService.getAllEvents();
	    }

    }

Queste poche righe contengono molte informazioni riguardo a come l'endpoint risponde:
- L'annotazione `@GET` implica che il metodo `getAllEvents` viene invocato se si effettua una richiesta GET all'URL `/events`
- L'annotazione `@Produces(MediaType.APPLICATION_JSON)` specifica il formato del body della risposta. In questo caso viene prodotto un JSON, ovviamente l'header `application-content` verrà settato di conseguenza.
- Se la richiesta va a buon fine, viene generata una risposta con codice `200: OK` e la lista di `Event` viene automaticamente serializzata.

JAX-RS ha quindi molti automatismi già pronti per supportare lo sviluppo, in modo da ridurre la produzione di codice ripetitivo all'interno degli endpoint. Sono supportati anche path dedicati ai singoli metodi, oltre a poter specificare parametri all'interno di essi:

    @PUT
	@Path("{eventId}/players/{playerId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response enrollPlayerInEvent(@PathParam("eventId") Long eventId, @PathParam("playerId") Long playerId) {
		return Response
				.accepted(eventService.enrollPlayerInEvent(playerId, eventId))
				.build();
	}

L'uso di `@PathParam` nella signature del metodo permette di recuperare il parametro specificato dentro l'annotazione `@Path`. Si noti in questo caso che JAX-RS permette anche di creare manualmente la risposta, specificando il codice di stato e l'eventuale body.

#### Architettura REST vs GraphQL
Un endpoint GraphQL affianca le API REST, in modo da far sfruttare i vantaggi di questa libreria ai client che lo supportano. La creazione qualsiasi Provider è immediata tramite l'uso dell'annotazione `GraphQLApi`, ed ha come ulteriore effetto l'esposizione dello stesso sull'URL `/graphql`.

![](graphql_resolvers.png)
##### Figura 2.1: I provider GraphQL rispondono tutti sullo stesso endpoint.

La creazione di query e di mutazioni è resa possibile dalle rispettive annotazioni `@Query` e `@Mutation`, che permettono di specificare anche il nome associato, in modo da disaccoppiare lo schema esposto dalle signature dei metodi.

    @GraphQLApi
    public class EventGraphQLProvider {
                
        @Query("eventById")
        public EventDetails getEventById(@Name("id") Long eventId) {
            return eventService.getEventById(eventId);
        }
        
        @Mutation("addEvent")
        public EventDetails addEvent(Event event) {
            return eventService.createEvent(event);
        }
    }

Le richieste GraphQL devono essere effettuate tutte tramite POST, mentre le risposte hanno sempre codice `200`, anche in presenza di errori.

### 2.2 Architettura
L'architettura del backend fa riferimento alle classiche implementazioni 3-tier. 
Sono stati previsti infatti tre livelli, contenuti tra gli endpoint e il database:

- **Service Layer**: offre una serie di servizi derivati dagli use cases, manipolando gli oggetti di dominio secondo le logiche di business. Sono presenti diversi servizi, ognuno con le sue responsabilità:
    - **UserService**: per l'interrogazione e la manipolazione dei profili utente.
    - **{Match,Event,Game}Service**: per l'organizzazione e l'aggiornamento delle varie fasi e componenti di un evento.
    - **{Authentication,Authorization}Service**: offrono servizi e meccanismi per identificare univocamente un utente - rilasciando e validando token JWT - oltre a determinare se possiede i diritti su una certa risorsa.    

- **Repository Layer**: media tra Service e Persistence, comportandosi come una collezione di oggetti in memoria nascondendo la complessità delle operazioni di persistenza necessarie al database. Permette inoltre di utilizzare collezioni presenti naturalmente in memoria come `GameRepositoryInMemory` mantenendo la stessa interfaccia.

- **Persistence Layer**: comprende i Data Access Object per ognuna delle entità del domain model. Offre un'interfaccia uniforme per Repository, in modo da astrarre da quale database si sta utilizzando.
    - `GenericDAO<T>` è un'interfaccia che descrive i principali metodi che un DAO dovrebbe possedere, identificati dalle normali operazioni CRUD.
    - `MySQLGenericDAO<T>` implementa l'interfaccia precedente utilizzando un Entity Manager e specializzando i suoi metodi rispetto all'uso di MySQL come database.
    - `UserDAO`, `EventDAO`, `MatchDAO` estendono `MySQLGenericDAO<T>` specificando il tipo a cui fanno riferimento. Possono aggiungere nuovi metodi oppure eseguire l'override di quelli già presenti.

#### Autenticazione e Autorizzazione
Alcune operazioni messe a disposizione dal servizio possono esporre dati personali o intaccare componenti fondamentali di un evento/torneo. Ovviamente devono essere messe in campo restrizioni e meccanismi per regolare l'accesso e identificare i client.

Vista la natura **stateless** del servizio l'utilizzo di **JWT** è apparso sensato: l'utente - autenticandosi - riceve un token univoco sotto forma di stringa firmato e riconosciuto dal sistema. Nel momento in cui richiede una certa operazione è sufficiente che si identifichi includendo il token all'interno di un header, in modo tale che il servizio estrapoli l'username associato e verifichi l'autorizzazione all'operazione richiesta.

JAX-RS mette a disposizione una varietà di strumenti per ovviare al problema *authorizazion/authenication* che supportano pienamente JWT. **ContainerRequestFilter** è un'interfaccia che permette di implementare filtri da eseguirsi dopo il matching della risorsa, in questo modo per esempio è possibile verificare che sia presente un header e che il contenuto sia un token JWT valido:

    class AuthenticationFilter implements ContainerRequestFilter {

        @Inject
        AuthenticationService authService;

        @Inject
        UserService userService;

        @Override
        public void filter(ContainerRequestContext requestContext) throws IOException {

            String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

            if(authorizationHeader == null)
                requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                .entity("You need to be authenticated.")
                .build());

            if (authorizationHeader.startsWith("Bearer ")) {
                String authenticationToken = authorizationHeader.substring(7);
                handleTokenBasedAuthentication(authenticationToken, requestContext);
            }

        }

    }

Il metodo `filter()` viene chiamato per ogni richiesta dove è richiesta l'autenticazione, abortendo nei casi in cui il token non sia valido (utente non riconosciuto, token scaduto o revocato...).

Per quanto concerne l'**autorizzazione** ad una certa risorsa si è deciso di basarla sul ruolo e sul concetto di possesso della stessa. User, Match, Event implementano il metodo `isOwnedBy(User user)` dell'interfaccia `Ownable`, che restituisce `true` qualora l'utente passato come argomento abbia diritto a modificare quella risorsa.
JAX-RS fornisce delle annotazioni in cui è possibile specificare quali ruoli abbiano accesso alla risorsa, utilizzando un ContainerRequestFilter, ma non sono sufficienti per implementare le politiche di autorizzazione senza effettuare un controllo nel Service di riferimento.

## Deployment
La messa in opera del servizio è stata pensata per essere il meno possibile dipendente dal sistema ospitante. L'uso di un application server semplifica di molto il deployment di un servizio, mentre Docker Compose aiuta a creare una rete di container già pronta da esporre, senza bisogno di configurazioni.

### 3.1 WildFly 20
WildFly è un application server modulare e leggero, nella versione base offre funzionalità essenziali e include implementazioni compliant con gli standard JAX-RS, CDI e JPA. Funzionalità aggiuntive possono essere abilitate tramite il file di configurazione o tramite l'installazione di moduli.

Per Arcano si è reso necessario fare alcune modifiche al file di configurazione.
- abilitare il supporto a GraphQL e JWT, aggiungendo le opportune estensioni nella sezione `<extensions>`.
- aggiungere il driver JDBC per MySQL nei moduli di WildFly.
- configurare un datasource diretto al database nella sezione `<datasources>`.

Di seguito viene mostrato un estratto del file di confiurazione che mostra come viene effettuata la connessione al database: la variabile `DB_URI` viene settata da Docker e corrisponde all'hostname del container che ospita il database.

````
    <datasource jta="true" jndi-name="java:/arcano-ds" pool-name="arcano-ds" enabled="true" use-ccm="true">
				  <connection-url>jdbc:mysql://${env.DB_URI}:3306/arcano?serverTimezone=UTC</connection-url>
				  <driver>mysql</driver>			
	</datasource>
````

Per l'occasione è stata realizzata un'immagine Docker, ottenuta estendendo l'immagine ufficiale di WildFly 20. Vengono aggiunti il file di configurazione, il driver e l'applicazione in formato `.war`. Viene inoltre specificato un utente per la console di amministrazione di WildFLy, oltre a inserire uno script che ritarda la partenza dell'application server finché il database non è pronto ad accettare connessioni.

### 3.2 Docker-Compose: orchestrazione dei servizi
La combinazione applicazione + database non rende facile la configurazione su un server, perciò la scelta di Compose è stata naturale: in questo modo è possibile replicare le stesse condizioni e settaggi su qualunque macchina lo si voglia eseguire.

![](container_docker.png)

I due elementi sono ospitati in container dedicati, possono comunicare liberamente all'interno della rete virtuale creata da Docker ma solamente le porte 8080 e 9990 sono esposte al di fuori della rete virtuale.