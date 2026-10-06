import { useEffect, useRef } from 'react';
import { railTracksApi, tripsApi, vehiclesApi, driversApi } from '../services/api';

export const useRailTripAutomation = (enabled: boolean) => {
  const hasInitialized = useRef(false);

  useEffect(() => {
    if (!enabled) return;
    if (hasInitialized.current) return;
    hasInitialized.current = true;

    const initializeRailTracks = async () => {
      try {
        const response: any = await railTracksApi.getAll();
        const tracks = response?.data || response;
        if (!tracks || (Array.isArray(tracks) && tracks.length === 0)) {
          const defaultTracks = [
            { trackNumber: 'Track 1 - North Terminal', capacity: 100, status: 'AVAILABLE' },
            { trackNumber: 'Track 2 - Bulk Cargo', capacity: 120, status: 'AVAILABLE' },
            { trackNumber: 'Track 3 - Container Yard', capacity: 150, status: 'AVAILABLE' },
            { trackNumber: 'Track 4 - East Wing', capacity: 80, status: 'AVAILABLE' },
            { trackNumber: 'Track 5 - South Gate', capacity: 200, status: 'AVAILABLE' },
          ];
          for (const t of defaultTracks) {
            await railTracksApi.create(t);
          }
        }
      } catch (error) {
        console.error('Failed to initialize rail tracks', error);
      }
    };

    initializeRailTracks();
  }, [enabled]);

  useEffect(() => {
    if (!enabled) return;

    const tripInterval = setInterval(async () => {
      try {
        const [vehiclesRes, driversRes]: [any, any] = await Promise.all([
          vehiclesApi.getAvailable(),
          driversApi.getAvailable(),
        ]);
        
        const vehicles = vehiclesRes?.data || vehiclesRes;
        const drivers = driversRes?.data || driversRes;
        
        if (Array.isArray(vehicles) && Array.isArray(drivers) && vehicles.length > 0 && drivers.length > 0) {
          const vehicle = vehicles[Math.floor(Math.random() * vehicles.length)];
          const driver = drivers[Math.floor(Math.random() * drivers.length)];
          
          const tripData = {
            vehicleId: vehicle.id,
            driverId: driver.id,
            source: 'Rail Yard ' + (Math.floor(Math.random() * 5) + 1),
            destination: 'Dock ' + (Math.floor(Math.random() * 10) + 1),
            cargoWeight: Math.floor(Math.random() * 20000) + 1000,
            priority: 'MEDIUM',
          };
          
          await tripsApi.create(tripData);
        }
      } catch (error) {
        console.error('Failed to generate trip', error);
      }
    }, 60000);

    return () => clearInterval(tripInterval);
  }, [enabled]);
};
