FROM nginx:stable-alpine

COPY web/config/default.conf /etc/nginx/conf.d/default.conf

COPY web/index.html /usr/share/nginx/html/index.html
