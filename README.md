# Test Task - тестовое задание
Репозиторий содержит моё выполнение тестового задания.

[![Typing SVG](https://readme-typing-svg.herokuapp.com?color=%2336BCF7&lines=Проект+на+стадии+поддержки)](https://git.io/typing-svg)

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)
![Apache Tomcat](https://img.shields.io/badge/apache%20tomcat-%23F8DC75.svg?style=for-the-badge&logo=apache-tomcat&logoColor=black)
![Maven](https://img.shields.io/badge/apachemaven-C71A36.svg?style=for-the-badge&logo=apachemaven&logoColor=white)
![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white)
![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

## Содержание
- [Технологии](#Технологии)
- [Описание задания](#Задание)
- [Быстрый старт](#Использование)


## Технологии
+ [Java 17](https://jdk.java.net/17/)
+ [Spring Boot](https://spring.io/projects/spring-boot)
+ [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
+ [Spring Security](https://spring.io/projects/spring-security)
+ [Spring Web Services](https://spring.io/projects/spring-ws)
+ [PostgreSQL](https://www.postgresql.org/)
+ [Hibernate](https://hibernate.org/)
+ [Tomcat](https://tomcat.apache.org/)
+ [Git](https://git-scm.com/)
+ [Docker](https://www.docker.com/)
+ [Lombok](https://projectlombok.org/)
+ [Mapstruct](https://mapstruct.org/)
+ [Maven](https://maven.apache.org/)
+ [Swagger](https://swagger.io/)
+ [JWT](https://jwt.io/)

## Задание

**Необходимо написать приложение**

Стек и тулы:

База данных Postgres
Java 11 версии и выше
Имплементация на Spring boot.
REST API.
Сборка: Maven

Структура таблиц БД (если резко необходимо, то можно добавить поля и таблицы)

| Key type | Column        | Type         | Unique | Comment                              |
|----------|---------------|--------------|--------|--------------------------------------|
|          | Table:        | **USER**     |        |                                      |
| PK       | ID            | BIGINT       | TRUE   |                                      |
|          | NAME          | VARCHAR(500) |        |                                      |
|          | DATE_OF_BIRTH |              |        | Format: 01.05.1993                   |
|          | PASSWORD      | VARCHAR(500) |        | Min length: 8, max 500               |
|          | Table:        | **ACCOUNT**  |        |                                      |
| PK       | ID            | BIGINT       | TRUE   |                                      |
| FK       | USER_ID       | BIGINT       | TRUE   | Link to USER.ID                      |
|          | BALANCE       | DECIMAL      |        | рубли + копейки: в коде – BigDecimal |
|          | Table:        | EMAIL_DATA   |        |                                      |
| PK       | ID            | BIGINT       | TRUE   |                                      | 
| FK       | USER_ID       | BIGINT       | TRUE   | Link to USER.ID                      |
|          | EMAIL         | VARCHAR(200) | TRUE   |                                      |
|          | Table:        | **PHONE_DATA |        |                                      |
| PK       | ID            | BIGINT       | TRUE   |                                      |
| FK       | USER_ID       | BIGINT       | TRUE   | Link to USER.ID                      |
|          | PHONE         | VARCHAR(13)  | TRUE   |                                      |

### Общие требования к системе:

1. 3 слоя – API, service, DAO
2. Будем считать, что в системе только обычные пользователи (не админы и т.д).
3. Будем считать, что пользователи создаются миграциями (не будем усложнять). Просто считаем, что для обычных пользователей нет операции создания. Для тестов создать напрямую в DAO.
4. У пользователя может быть более одного PHONE_DATA (должен быть как минимум 1).
5. У пользователя может быть более одного EMAIL_DATA (должен быть как минимум 1).
6. У пользователя должен быть строго один ACCOUNT.
7. Начальный BALANCE в ACCOUNT указывается при создании пользователя.
8. BALANCE в ACCOUNT не может уходить в минус ни при каких операциях.
9. Валидация входных API данных.

### Обязательные фичи:

 1. ***CREATE*** (только для определенных внутри пользователя данных), **_UPDATE_** операции для пользователя. Пользователь может менять только собственные данные:
* может удалить/сменить/добавить email если он не занят другим пользователям
* может удалить/сменить/добавить phone если он не занят другим пользователям
* остальное менять не может
2. Реализовать **_READ_** операцию для пользователей. Сделать «поиск пользователей» (искать может любой любого) с фильтрацией по полям ниже и пагинацией (size, page/offset):
* Если передана «dateOfBirth», то фильтр записей, где «date_of_birth» больше чем переданный в запросе.
Если передан «phone», то фильтр по 100% сходству.
* Если передан «name», то фильтр по like форматом ‘{text-from-request-param}%’
* Если передан «email», то фильтр по 100% сходству.
3. Добавить JWT token (необходимый Claim только USER_ID), механизм получения токена на ваше усмотрение. Имплементировать максимально просто, не стоит усложнять. Аутентификация может быть по email+password, либо по phone+password.
4. Раз в 30 секунд BALANCE каждого клиента увеличиваются на 10% но не более 207% от начального депозита.
_Например:_
_Было: 100, стало: 110.
Было: 110, стало:121._
…
5. Сделать функционал трансфер денег от одного пользователя к другому.
Вход: USER_ID (transfer from) – берем у авторизованного из Claim токена, USER_ID (transfer to) из запроса, VALUE (сумма перевода) из запроса.
То есть у того, кто переводит мы списываем эту сумму, у того, кому переводим – добавляем эту сумму.
Считать эту операцию «банковской» (высоко-значимой), сделать ее со всеми нужными валидациями (надо подумать какими) и потоко-защищенной.

### **Не обязательные фичи (но которые очень хотелось бы видеть, хотя бы в минимальном исполнении):**

1. Добавить swagger (минимальную конфигурацию).
2. Добавить не бездумное (значимое) логгирование.
3. Добавить корректное кэширование (например на API и на DAO слой). Имплементация на ваше 
усмотрение.

### **Результат:**

Результат тестового задания необходимо предоставить в виде ссылки на публичный репозиторий на 
Гитхабе.

## Использование

- Установите [Docker](https://docs.docker.com/get-docker/) и [Docker Compose](https://docs.docker.com/compose/install/)
- Установите [JDK 17](https://jdk.java.net/17/)
- Установите [Maven](https://maven.apache.org/install.html)

### Шаги для запуска:
1. **Соберите проект**:
   ```bash
   mvn clean install

2. Запустите сервисы через Docker Compose:
    ```bash
    docker-compose up --build -d

3. Проверьте статус сервисов:
   ```bash
   dockercompose ps

4. Тестирование API можно осуществить с помощью [Swagger](http://localhost:8080/swagger-ui/index.html#/).
   5. Также, вы можете взаимодействовать с API с помощью Postman. Внутри корневой директории проекта хранится файл: ```GK.postman_collection.json```, импортируйте эту коллекцию в свой Postman, чтобы получить доступ к библиотеке запросов. 