import cors from 'cors';
import express from 'express';

import { chatRouter } from './chat/chat.router.js';

const app = express();

const PORT = 3000;

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
  PORT,
  () => {
    console.log(
      `Petora API running on http://localhost:${PORT}`,
    );
  },
);