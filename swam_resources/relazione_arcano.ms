.TL
Arcano - backend per l'organizzazione e la gestione di eventi sportivi
.AU
Riccardo Malavolti
.AI
Università degli Studi di Firenze - A.A. 2020/2021

.AB
Enumerare tutte le tipologie e varietà di giochi di carte è un'impresa destinata ad occupare molto tempo: oltre ai "classici", diffusi e conosciuti dalla quasi totalità delle persone, negli ultimi decenni hanno spopolato nuove tipologie di carte, che uniscono la componente tradizionale del gioco con quella del collezionismo. I giochi di carte collezionabili (GCC) hanno riscosso un enorme successo, arrivando a organizzare tornei con migliaia di partecipanti con montepremi multimilionari. 
Con l'elaborato presentato si vuole provare a fornire una soluzione off-line per l'organizzazione e la gestione di questi tornei, adatta sia a piccoli ritrovi che a grandi manifestazioni. Arcano è il primo tassello di questa soluzione, un backend scritto in Java che dovrà essere sfruttato dall'applicazione Congrega, in dotazione ai giocatori e organizzatori del torneo.
.PP
Questa relazione illustra innanzitutto i requisiti funzionali dettati dal dominio di applicazione, i casi d'uso e la creazione del domain model. 
.AE

.NH
Una soluzione collaborativa
.PP
Le community di gioco sono abbastanza efficienti nell'organizzare tornei, che si tratti di un gruppo di amici o di un palazzetto dello sport stracolmo di partecipanti gli organizzatori riescono quasi sempre a gestire iscrizioni, fasi, gironi e punteggi. Nel caso di 
.I "Magic: The Gathering" 
la società che possiede il marchio organizza in prima persona i tornei con i montepremi più ricchi, ma permette a privati accreditati (perlopiù negozi o associazioni di medie dimensioni) di organizzare eventi sanzionati concedendo un software per la registrazione dei punti e la gestione dell'evento, purché sia presente un arbitro accreditato. Questo software però è obsoleto, difficile da configurare e per nulla 
.I "user-friendly"
, ma la vera mancanza è quella di non poter garantire un accesso ai dati per giocatori presenti ed iscritti. L'unico modo di avere uno sguardo sull'andamento dell torneo è quello di controllare le graduatorie alla fine di ogni girone, che vengono stampate e affisse.
.PP
.B Arcano
nasce con l'obiettivo di restituire un po' trasparenza nei riguardi dei giocatori: offrendo sia una piattaforma per la gestione dell'evento, sia un punto di accesso per la consultazione di statistiche e informazioni in tempo reale ai partecipanti della manifestazione.

.NH 2
Requisiti e casi d'uso
.PP
I requisiti funzionali del servizio provengono da più attori:
.IP \[bu]
.B Giocatori:
sono coloro che effettivamente partecipano all'evento. Sono responsabili del loro account (contenente le loro informazioni personali) e del'aggiornamento dello stato di ogni match che li riguarda. Possono convocare un Giudice per risolvere diatribe o chiedere chiarimenti. 
.IP \[bu]
.B Giudici:
regolano e sanzionano l'evento, risolvendo i conflitti tra giocatori quando convocati. Possono assegnare punteggi o decretare lo stato un di match o di un gioco.
.IP \[bu]
.B Amministratori: 
organizzano e monitorano l'evento, creando le fasi e i gironi. Possono espellere i giocatori e applicare le sanzioni imposte dai giudici. 

Lo schema in figura [1.1] riassume i casi d'uso derivati dai requisiti sopraelencati.

\" .PDFPIC /path/to/use_case_diagram

.NH 2
Modello di dominio
.PP

.NH 2
Design delle API
.PP

.NH 
Implementazione del backend

.NH 2
Implementazione degli endpoint
.NH 3
Architettura REST vs GraphQL

.NH 2
Architettura N-layered

.NH 3
Service Layer
.NH 3
Repository Layer
.NH 3
Persistence Layer

.NH 2
Autenticazione e Autorizzazione

.NH
Deployment
.NH 2
WildFly 20
.NH 2
Docker-Compose: orchestrazione dei servizi

