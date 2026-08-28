import { z } from 'zod';

export const chatRequestSchema = z.object({
  message: z
    .string()
    .trim()
    .min(1)
    .max(4000),

  petId: z
    .string()
    .trim()
    .min(1),
});

export type ChatRequest = z.infer<
  typeof chatRequestSchema
>;