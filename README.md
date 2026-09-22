# EduShare - Backend API

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)

## 🎓 Contesto
Progetto sviluppato per il corso di Tecnologie Web presso il Corso di Laurea in Informatica dell'Università degli Studi di Torino.
Servizio di backend per la piattaforma e-learning EduShare. Gestisce l'autenticazione degli utenti con rispettivi ruoli, la persistenza dei dati e l'erogazione dei contenuti didattici.

> ⚠️ **Architettura Disaccoppiata:** Questo repository contiene esclusivamente il server REST API sviluppato in Java/Spring Boot. 
> L'interfaccia utente (Client React) associata a questo progetto è disponibile qui: [EduShare Frontend](https://github.com/AleMal03/EduShareProj-frontend)

## 🏗 Architettura & Design Pattern
- **Pattern MVC:** Separazione rigorosa tra Model, View e Controller REST.
- **Sicurezza:** Autenticazione stateful gestita tramite `HttpSession` e autorizzazione Role-Based (Docente / Studente).
  > ⚠️ **Nota:** l'applicativo allo stato attuale è un mock pensato esclusivamente per fini didattici, pertanto non è impostata alcuna forma di protezione o hashing della password.
- **ORM:** Utilizzo di Hibernate per la mappatura degli oggetti nel DB relazionale.

## ✨ Funzionalità
* **Ruolo Studente:** Ricerca dinamica dei corsi con filtri avanzati, iscrizione e modifica dati del profilo.
* **Ruolo Docente:** Creazione e rimozione dei propri corsi.
* **REST API:** Endpoint documentati e strutturati in modo RESTful.

## 🔌 Endpoint Principali
| Metodo | Endpoint | Descrizione | Autenticazione |
|---|---|---|---|
| `GET` | `/corsi` | Ritorna la lista dei corsi applicando i filtri passati come parametri. | ❌ |
| `GET` | `/corsi/seguiti` | Ritorna i corsi a cui l'utente loggato è iscritto. | ✅ |
| `GET` | `/corsi/miei` | Ritorna i corsi creati dall'insegnante loggato. | ✅ (Solo Insegnanti) |
| `POST` | `/corsi/miei/aggiungi` | Crea un nuovo corso. | ✅ (Solo Insegnanti) |
| `POST` | `/corsi/miei/rimuovi` | Elimina un corso esistente creato dall'utente. | ✅ (Solo Insegnanti) |
| `GET` | `/corsi/materie` | Ritorna l'elenco di tutte le materie disponibili.| ❌ |
| `GET` | `/corsi/difficolta` | Ritorna i gradi di difficoltà previsti dal sistema. | ❌ |
| `GET` | `/corsi/maxCosto` | Ritorna il costo massimo tra tutti i corsi nel DB. | ❌ |
| `POST` | `/corsi/iscrizione` | Iscrive l'utente corrente al corso specificato. | ✅ |
| `POST` | `/corsi/seguiti/disiscrizione` | Rimuove l'iscrizione dell'utente a un corso. | ✅ |
| `GET` | `/files?idCorso={id}` | Ritorna tutti i file didattici associati al corso specificato. | ❌ |
| `POST` | `/modify_data/email` | Aggiorna l'indirizzo email dell'utente. | ✅ |
| `POST` | `/modify_data/password` | Aggiorna la password dell'utente. | ✅ |
| `POST` | `/modify_data/fotoProfilo` | Aggiorna l'immagine del profilo. | ✅ |
| `POST` | `/modify_data/aboutMe` | Aggiorna la biografia/descrizione. | ✅ (Solo Insegnanti) |
| `POST` | `/modify_data/lingueParlate` | Modifica il set di lingue parlate dall'utente. | ✅ |
| `POST` | `/modify_data/titoliStudio` | Modifica i titoli accademici/professionali. | ✅ (Solo Insegnanti) |

## 🚀 Installazione e Avvio
**Prerequisiti di Sistema**
*   **Java Development Kit (JDK):** Versione 21.
*   **Database:** PostgreSQL in esecuzione sulla porta `5432` locale.
*   **Build Tool:** Maven.

**Configurazione del Database**
Prima di avviare l'applicazione, assicurarsi di configurare l'istanza PostgreSQL per accettare le connessioni previste dal sistema. Creare un database e un utente con le seguenti credenziali di default (modificabili nel file `application.properties`):
*   **Database:** `EduShareDB`
*   **Username:** `EduShareAdmin`
*   **Password:** `admin`

> ⚠️ **Nota sullo schema DB:** Il parametro `spring.jpa.hibernate.ddl-auto` è attualmente impostato su `create`. Ad ogni riavvio del server, lo schema del database verrà distrutto e ricreato da zero. Impostarlo su `update` per mantenere i dati persistenti.

**Avvio del Server**
1. Clonare il repository in locale e posizionarsi nella cartella root del progetto:
   ```bash
   git clone https://github.com/AleMal03/EduShareProj-backend
   cd EduShareProj-backend
   ```
2. Eseguire la build e avviare il server tramite Maven:
   ```bash
   mvn spring-boot:run
   ```
Il server sarà in ascolto sulla porta 7777.

🔒 **Nota sui Cookie di Sessione**: L'applicazione utilizza il flag `secure=true` per i cookie di sessione (`EDUSHARE_SESSION_COOKIE`). Se si testano le chiamate API tramite Postman in locale senza protocollo HTTPS, assicurarsi di disabilitare temporaneamente questo flag per permettere al client di salvare correttamente la sessione.
