# ROTATIO REST API

## Opis
Jest to backendowa część aplikacji odpowiedzialna za dostarczanie danych poprzez REST API.

## Wymagania systemowe
- Java 17
- Gradle lub Maven
- MySQL lub inna baza danych kompatybilna z JDBC

## Instalacja
1. Sklonuj repozytorium:
   ```bash
   git clone https://github.com/likeahim/rotatio-app-b.git
   cd backend-rest-api

2. Utwórz bazę danych i dodaj jej dane w application.properties (znajdziesz w src/main/resources).
3. Zainstaluj zależności i uruchom aplikację:
   `./gradlew bootRun`

## Testowanie
Aby uruchomić test, użyj polecenia:
`./gradlew test`

## Powiązane projekty
Aby uzyskać dostęp do frontendu tej aplikacji opartego na Vaadin, odwiedź: https://github.com/likeahim/rotatio-app-f

## Licencja
Ten projekt jest licencjonowany na zasadach MIT. Szczegóły znajdują się w pliku LICENSE.
