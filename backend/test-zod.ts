import { z } from 'zod';
const vehicleSchema = z.object({
  registrationNo: z.string().min(3),
  name: z.string().min(2),
  model: z.string().min(2),
  type: z.enum(['TRUCK', 'TRAILER', 'FORKLIFT', 'REACH_STACKER', 'CRANE_TRUCK']),
  maxCapacity: z.number().positive(),
  odometer: z.number().nonnegative().optional(),
  fuelLevel: z.number().min(0).max(100).optional(),
  healthScore: z.number().min(0).max(100).optional(),
  status: z.enum(['AVAILABLE', 'ON_TRIP', 'IN_SHOP', 'RETIRED']).optional(),
});
const body = {
  registrationNo: "TEST-123",
  name: "Test",
  model: "2024",
  type: "TRUCK",
  maxCapacity: 40
};
console.log(vehicleSchema.parse(body));
