# Yoga App — Guide d’installation, exécution, tests & couverture (Back + Front)

Ce document explique **comment installer la base de données**, **installer et lancer l’application (back & front)**, **exécuter les tests (unitaires, d’intégration & e2e)** et **générer les rapports de couverture**.

> Arborescence supposée :
> ```P5-Full-Stack-testing
> /
> ├─ back/      # Spring Boot (Maven)
> └─ front/     # Angular (npm + Cypress)
> ```

---

## 1) Prérequis

### Back (Spring Boot)
- **Java 17** (ou 11 minimum)
- **Maven 3.8+**

### Front (Angular)
- **Node.js 18+** (LTS recommandé)
- **npm 9+**
- **Angular CLI** : `npm i -g @angular/cli`
- **Cypress** (installé localement via `npm i`, pas besoin globalement)

---

## 2) Base de données (Back)

Dans `back/src/main/resources/application.properties` (ou `application-local.properties`), adaptez :
   ```properties
spring.datasource.url=jdbc:mysql://localhost:3306/yoga?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=yoga
spring.datasource.password=yoga
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect

spring.sql.init.mode=always

   ```


---

## 3) Installer & lancer le Back

Depuis le dossier `P5-Full-Stack-testing/` :
```bash
cd back
mvn clean install
mvn spring-boot:run
```
- L’API démarre par défaut sur **http://localhost:8080**.

### Tests back
Exécuter tous les tests (unitaires + d’intégration) :
```bash
mvn clean test
```

### Couverture (JaCoCo)
Générer le rapport JaCoCo (HTML) :
```bash
mvn clean test jacoco:report
```
- Rapport : `back/target/site/jacoco/index.html`  
- Rapport XML (CI) : `back/target/site/jacoco/jacoco.xml`

### Vérifier le seuil de couverture
Si un seuil (ex. 80 % lignes/branches) est configuré dans `pom.xml` via `jacoco-maven-plugin`, un build échouera si le seuil n’est pas atteint. Pour produire le check :
```bash
mvn clean verify
```
Rapports utiles de tests :
- **Surefire** (tests unitaires) : `back/target/surefire-reports/`
- **Failsafe** (si IT séparés) : `back/target/failsafe-reports/`

---

## 4) Installer & lancer le Front (Angular)

Depuis le dossier `P5-Full-Stack-testing/` :
```bash
cd front
npm install       
npm run start     # alias généralement pour: ng serve
# ou directement: ng serve --open
```
- L’app démarre sur **http://localhost:4200** (par défaut).

### Tests unitaires (Jest) + Couverture
```bash
# Lancer les tests
npm run test

# Générer la couverture
npm run test -- --coverage
```
- Rapport HTML : `front/coverage/lcov-report/index.html`
- Rapport LCOV : `front/coverage/lcov.info` (utile pour Sonar/CI)


### Tests End‑to‑End (Cypress)
#### Lancer le runner interactif
```bash
npx cypress open
# choisir E2E, sélectionner le navigateur, puis lancer les specs
```
#### Lancer en mode headless (CI)
```bash
npx cypress run
# ou avec un navigateur spécifique:
npx cypress run --browser chrome
```

#### Couverture avec Cypress (optionnel)
Si le projet intègre `@cypress/code-coverage` :
1. Assurez-vous que le front est instrumenté (Istanbul) côté build dev/test.
2. Exécutez les e2e ; les rapports apparaîtront en général dans `front/coverage/`,
   fusionnés avec la couverture unitaires selon la config `nyc`/`istanbul`.

---

## 5) Lancer l’application complète

1. **Back** :
   ```bash
   cd back
   mvn spring-boot:run
   ```
2. **Front** :
   ```bash
   cd front
   npm start
   ```
3. Ouvrez **http://localhost:4200**. Le front communique avec le back sur **http://localhost:8080** (adaptez l’URL d’API dans l’environnement Angular si nécessaire : `front/src/environments/*`).

---

## 6) Générer tous les rapports de couverture (résumé rapide)

- **Back (JaCoCo)** :
  ```bash
  cd back
  mvn clean test jacoco:report
  # HTML: back/target/site/jacoco/index.html
  ```

- **Front (jest)** :
  ```bash
  cd front
 npm run test
 npm run test -- --coverage
  # HTML: P5-Full-Stack-testing/front/coverage/lcov-report/index.html
  ```

- **Front e2e (Cypress + code-coverage)** *(si configuré)* :
  ```bash
  cd front
  npx cypress run
  # Rapports: P5-Full-Stack-testing/front/coverage/e2e/lcov-report/index.html
  ```

---


## 8) Licence & crédits

Projet pédagogique « Yoga App ». Utilise Spring Boot, Angular, Cypress, JaCoCo & Istanbul.
