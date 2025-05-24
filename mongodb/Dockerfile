FROM mongo:7.0

# Copia seed.js nel punto previsto da Mongo per l'inizializzazione
COPY ./mongodb/seed.js /docker-entrypoint-initdb.d/seed.js
