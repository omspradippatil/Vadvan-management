import rateLimit from 'express-rate-limit';

export const generalLimiter = (req: any, res: any, next: any) => next();
export const authLimiter = (req: any, res: any, next: any) => next();
