import 'dotenv/config';

import { z } from 'zod';

const envSchema = z.object({
  OPENAI_API_KEY: z
    .string()
    .min(1),

  OPENAI_MODEL: z
    .string()
    .default('gpt-5.6-terra'),

  PORT: z.coerce
    .number()
    .int()
    .positive()
    .default(3000),
});

export const env = envSchema.parse(
  process.env,
);