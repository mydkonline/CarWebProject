FROM node:20-alpine

WORKDIR /app
COPY package*.json ./
RUN npm ci

COPY . .

ENV EXPO_NO_TELEMETRY=1
EXPOSE 19006

CMD ["npm", "run", "web", "--", "--host", "lan", "--port", "19006"]