# ---------- Frontend: React + Vite, serwowany przez nginx ----------

# Etap 1: budowanie aplikacji (vite build -> /app/dist)
FROM node:22-alpine AS build
WORKDIR /app

COPY package.json package-lock.json ./
RUN npm ci

COPY . .
RUN npm run build

# Etap 2: nginx z gotowymi plikami statycznymi + proxy do backendu
FROM nginx:1.27-alpine

COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80
