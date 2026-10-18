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

  previousResponseId: z
    .string()
    .trim()
    .min(1)
    .optional(),

   place: z
     .object({
       name: z.string().trim().min(1),
       latitude: z.number(),
       longitude: z.number(),
     })
     .optional(),
});

export type ChatRequest = z.infer<
  typeof chatRequestSchema
>;