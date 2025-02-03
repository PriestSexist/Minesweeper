# Используем официальный образ Maven с JDK 17
FROM maven:latest AS build

# Копируем исходный код в контейнер
COPY . /app

# Переходим в директорию с исходным кодом
WORKDIR /app

# Выполняем команду для сборки проекта
RUN mvn clean package -DskipTests

# Используем лёгкий образ JDK для финального контейнера
FROM openjdk:17-jdk-slim

# Копируем собранный JAR файл из контейнера сборки
COPY --from=build /app/target/*.jar /app.jar

# Устанавливаем точку входа для запуска приложения
ENTRYPOINT ["java", "-jar", "/app.jar"]