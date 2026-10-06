import { useEffect } from 'react';
import { vehiclesApi, maintenanceApi, fuelApi } from '../services/api';

export function useMaintenanceFuelAutomation(enabled = true) {
  useEffect(() => {
    if (!enabled) return;

    let timeoutId: ReturnType<typeof setTimeout>;

    const runAutomation = async () => {
      try {
        // Fetch a list of vehicles
        const response: any = await vehiclesApi.getAll();
        
        // Handle standard axios wrapper data shapes
        const vehicles = Array.isArray(response) 
          ? response 
          : response?.data || response?.vehicles || [];

        if (vehicles && vehicles.length > 0) {
          const randomVehicle = vehicles[Math.floor(Math.random() * vehicles.length)];
          const vehicleId = randomVehicle.id || randomVehicle._id;

          if (vehicleId) {
            const isMaintenance = Math.random() > 0.5;

            if (isMaintenance) {
              const types = ['INSPECTION', 'REPAIR', 'SCHEDULED', 'EMERGENCY'];
              const type = types[Math.floor(Math.random() * types.length)];
              
              await maintenanceApi.create({
                vehicleId,
                type,
                description: `Automated ${type.toLowerCase()} maintenance check.`,
                cost: Math.floor(Math.random() * 1000) + 100,
                technicianName: 'AutoBot-X',
                scheduledAt: new Date().toISOString(),
              });
            } else {
              const exceptions = ['Low fuel', 'Abnormal consumption', 'Normal', 'Leak suspected'];
              const exception = exceptions[Math.floor(Math.random() * exceptions.length)];
              const isNormal = exception === 'Normal';

              await fuelApi.create({
                vehicleId,
                quantityLitres: Math.floor(Math.random() * 100) + 20,
                costPerLitre: 1.5,
                mileage: Math.floor(Math.random() * 10) + 5,
              });
            }
          }
        }
      } catch (error) {
        // Fail gracefully so it doesn't crash the app if an endpoint is busy
        console.warn('Automation cycle failed (likely network or busy endpoint):', error);
      }

      // Schedule next run between 30 and 45 seconds (30000ms to 45000ms)
      const delay = Math.random() * 15000 + 30000;
      timeoutId = setTimeout(runAutomation, delay);
    };

    // Initial delay to avoid immediately hitting endpoints on mount
    timeoutId = setTimeout(runAutomation, Math.random() * 5000 + 2000);

    return () => clearTimeout(timeoutId);
  }, [enabled]);
}
