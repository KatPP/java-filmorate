-- Получение всех фильмов с жанрами и рейтингом
SELECT f.*, g.name as genre_name, m.name as mpa_rating_name
FROM films f
         LEFT JOIN film_genres fg ON f.film_id = fg.film_id
         LEFT JOIN genres g ON fg.genre_id = g.genre_id
         LEFT JOIN mpa_ratings m ON f.mpa_rating_id = m.mpa_rating_id;

-- Получение топ N популярных фильмов
SELECT f.*, COUNT(l.user_id) as likes_count
FROM films f
         LEFT JOIN likes l ON f.film_id = l.film_id
GROUP BY f.film_id
ORDER BY likes_count DESC
LIMIT ?;

-- Получение списка друзей пользователя
SELECT u.*, f.status
FROM users u
         JOIN friendships f ON u.user_id = f.friend_id
WHERE f.user_id = ?
  AND f.status = 'CONFIRMED';

-- Получение общих друзей двух пользователей
SELECT u.*
FROM users u
         JOIN friendships f1 ON u.user_id = f1.friend_id
         JOIN friendships f2 ON u.user_id = f2.friend_id
WHERE f1.user_id = ?
  AND f2.user_id = ?
  AND f1.status = 'CONFIRMED'
  AND f2.status = 'CONFIRMED';