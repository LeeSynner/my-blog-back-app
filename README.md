API для блога
2) Фронтенд работает на веб-сервере Nginx, принимает запросы от браузера пользователя на http://localhost:80/, отправляет запросы на получение данных в бэкенд по REST.
2) Бэкенд работает в сервлет-контейнере Tomcat/Jetty, принимает REST-запросы от фронтенда на http://localhost:8080/, отправляет SQL-запросы на создание/получение/изменение/удаление данных в БД PostgreSQL/H2 или любой другой, формирует ответ и возвращает фронтенду.
3) База данных хранит данные, обрабатывает запросы от бэкенда и возвращает результат.

Доступные ручки:
baseURL - http://localhost:8080/
1) Получить все посты с фильтром - GET {{baseURL}}/api/posts?search=&pageNumber=2&pageSize=1
2) Получить пост по id - GET {{baseURL}}/api/posts/{id}
3) Создать пост - POST {{baseURL}}/api/posts
4) Изменить пост - PUT {{baseURL}}/api/posts/{id}
5) Поставить лайк посту - POST {{baseURL}}/api/posts/{id}/likes
6) Добавить картинку посту - PUT {{baseURL}}/api/posts/{id}/image
7) Получить картинку поста - GET {{baseURL}}/api/posts/{id}/image
8) Получить все комментарии поста - GET {{baseURL}}/api/posts/{id}/comments
9) Получить комментарий поста по id - GET {{baseURL}}/api/posts/{post_id}/comments/{id}
10) Создать комментарий - POST {{baseURL}}/api/posts/{post_id}/comments
11) Изменить комментарий - PUT {{baseURL}}/api/posts/{post_id}/comments/{id}
12) Удалить комментарий - DELETE {{baseURL}}/api/posts/{post_id}/comments/{id}