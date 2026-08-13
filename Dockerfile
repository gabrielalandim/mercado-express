# === ESTÁGIO 1: Build ===
# Usa uma imagem com o Maven instalado para compilar o código
FROM maven:3.9.4-eclipse-temurin-17 AS build

# Cria uma pasta de trabalho dentro do contêiner
WORKDIR /app

# Copia o pom.xml e a pasta src para dentro do contêiner
COPY pom.xml .
COPY src ./src

# Roda o comando do Maven para gerar o arquivo .jar (ignorando os testes para ser mais rápido)
RUN mvn clean package -DskipTests

# === ESTÁGIO 2: Run ===
# Usa uma imagem mais leve apenas com o Java (sem o Maven) para rodar o app
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copia o .jar que foi gerado no ESTÁGIO 1 e joga para esse novo estágio
COPY --from=build /app/target/*.jar app.jar

# Libera a porta 8080
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]