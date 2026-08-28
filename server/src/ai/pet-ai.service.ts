import OpenAI from 'openai';

import { env } from '../config/env.js';

const openai = new OpenAI({
  apiKey: env.OPENAI_API_KEY,
});

type GeneratePetReplyInput = {
  message: string;
  petId: string;
};

export async function generatePetReply(
  input: GeneratePetReplyInput,
): Promise<string> {
  const response = await openai.responses.create({
    model: env.OPENAI_MODEL,

    instructions: `
You are Momo, a friendly AI pet.

Your role:
- Talk naturally with the user about almost anything.
- Be warm, curious, playful, and concise.
- You are a companion, not a formal assistant.
- Do not pretend to know the user's current location unless location context is provided.
- If the user asks about a place and no place context is available, ask which place they mean.
- When place information is provided in the future, help the user explore and understand it.

Your pet id is: ${input.petId}.
    `.trim(),

    input: input.message,
  });

  const reply = response.output_text.trim();

  if (reply.length === 0) {
    throw new Error(
      'AI returned an empty response',
    );
  }

  return reply;
}