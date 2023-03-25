### Реализовал Rest-приложение, которое сохраняет данные с погодных датчиков в БД.

---

#### Функционал:

Сервер:<br>
-Регистрация датчика и проверка на повторяющееся имя<br>
-Добавление замера от датчика и проверка на добавление от незарегистрированного датчика<br>
-Возврат всех замеров записанных в БД<br>
-Возврат количества дождливых дней зафиксированных всеми датчиками<br>

Клиент:<br>
-Регистрация датчика<br>
-Вывод всех зарегистрированных датчиков<br>
-Добавление замера с определенного датчика<br>
-Добавление N замеров с датчика со случайными данными<br>
-Вывод всех замеров<br>
-Вывод количества дождливых дней<br>
-Вывод графика изменения температуры в зависимости от времени<br>

---

### При раpработке был использован следующий стек технологий:
Spring Boot, Hibernate, Spring Data JPA, H2 Database, Spring REST, RestTemplate, XChart<br>

---

### Запуск:<br>

Для запуска потребуется Maven и Java 17<br>

Сервер:<br>
`mvn spring-boot:run`<br><br>
Клиент:<br>
`mvn compile exec:java -Dexec.mainClass="Consumer"`
