FROM node:8-alpine

ENV NODE_ENV=production

RUN mkdir /app
WORKDIR /app

COPY package.json .

RUN npm install --production

COPY . .

# Execute moleculer-runner
CMD ["node", "./node_modules/moleculer/bin/moleculer-runner.js"]