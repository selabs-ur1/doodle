FROM alpine:latest

RUN apk update
RUN apk add nginx
# RUN mkdir -p /run/nginx
COPY ./dist/tlcfront /front
COPY ./nginx.conf /etc/nginx/http.d/default.conf

WORKDIR /front

EXPOSE 80
CMD nginx -g "daemon off;"

