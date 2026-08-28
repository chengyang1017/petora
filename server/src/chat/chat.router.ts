import { Router } from 'express';

import { generatePetReply } from '../ai/pet-ai.service.js';
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

    try {
      const reply = await generatePetReply({
        message,
        petId,
      });

      response.json({
        reply,
      });
    } catch (error) {
      console.error(
        'Failed to generate pet reply:',
        error,
      );

      response.status(502).json({
        error: 'AI_SERVICE_ERROR',
        message: 'Failed to generate pet reply',
      });
    }
  },
);