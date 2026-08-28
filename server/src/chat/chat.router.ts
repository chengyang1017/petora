import { Router } from 'express';

import { chatRequestSchema } from './chat.schema.js';

export const chatRouter = Router();

chatRouter.post(
  '/',
  async (request, response) => {
    const result = chatRequestSchema.safeParse(
      request.body,
    );

    if (!result.success) {
      response.status(400).json({
        error: 'INVALID_CHAT_REQUEST',
        message: 'Invalid chat request',
      });

      return;
    }

    const {
      message,
      petId,
    } = result.data;

    response.json({
      reply: `Woof! I am ${petId}. You said: ${message}`,
    });
  },
);