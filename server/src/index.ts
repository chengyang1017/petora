import cors from 'cors';
import express from 'express';

import { chatRouter } from './chat/chat.router.js';
import { env } from './config/env.js';

const app = express();

app.use(cors());

app.use(
  express.json({
    limit: '1mb',
  }),
);

app.get(
  '/health',
  (_request, response) => {
    response.json({
      status: 'ok',
    });
  },
);

app.use(
  '/api/chat',
  chatRouter,
);

app.listen(
  env.PORT,
  () => {
    console.log(
      `Petora API running on http://localhost:${env.PORT}`,
    );
  },
);