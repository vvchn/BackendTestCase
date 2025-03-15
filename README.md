# BackendTestСase

## Бэкенд-приложение, представляющее собой универсальное решение для выполнения CRUD-операций над справочниками и их данными


## Инструкция по запуску

### 1. Локально на примере IDE IntelliJ IDEA 2024.3.4.1 (Community Edition)
- Должна быть установлена и запущена на локальной машине PostgreSQL актуальной версии.
- Установить JDK 17 версии.
- Склонировать к себе репозиторий (наиболее актуальная ветка — `main`).
- Добавить в конфигурацию запуска проекта `DB_URL`, `DB_USER` и `DB_PASSWORD`.
    - Например: `DB_URL=jdbc:postgresql://db:5432/dictionaries_db;DB_USER=postgres;DB_PASSWORD=password`.
- Запустить проект, используя эту конфигурацию запуска (`app`).

### 2. С использованием Docker (Docker Compose)
- Должен быть установлен Docker и Docker Compose актуальной версии.
- Склонировать к себе репозиторий (наиболее актуальная ветка — `main`).
- Создать в корне проекта файл `.env`.
- Добавить в этот файл `DB_URL`, `DB_USER`, `DB_PASSWORD`, `POSTGRES_DB`, `POSTGRES_USER` и `POSTGRES_PASSWORD`.
    - Например:
      ```plaintext
      DB_URL=jdbc:postgresql://db:5432/dictionaries_db
      DB_USER=postgres
      DB_PASSWORD=password
      POSTGRES_DB=dictionaries_db
      POSTGRES_USER=postgres
      POSTGRES_PASSWORD=password
      ```
- Перейти в корень проекта и выполнить в терминале:
    ```sudo docker compose up --build```

### 3. После запуска Swagger UI доступен по адресу:
###  ```http://localhost:8080/swagger/index.html```



### В проект добавлен статический анализатор кода (Detekt). Для анализа нужно выполнить:
### ```gradle detektMain```
### Результаты анализа сохраняются по пути ```/detekt/reports```

### Для запуска тестов нужно выполнить: ```gradle :test```