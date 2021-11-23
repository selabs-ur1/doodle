FROM alpine:latest

RUN apk update && apk add nginx

COPY ./dist/tlcfront /front
COPY ./nginx.conf /etc/nginx/http.d/default.conf

WORKDIR /front
EXPOSE 80

CMD echo "Démarrage du serveur NGINX sur le port 80..." && nginx -g "daemon off;"

