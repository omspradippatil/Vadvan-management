import { useEffect, useRef } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { shipsApi } from '../services/api';

const REAL_SHIP_NAMES = [
  'MSC Oscar',
  'Ever Given',
  'CMA CGM Jacques Saade',
  'HMM Algeciras',
  'Madrid Maersk'
];

export function useShipAutomation(enabled: boolean = true) {
  const queryClient = useQueryClient();
  const automationTriggered = useRef(false);

  // Fetch all existing ships
  const { data: ships, isSuccess } = useQuery({
    queryKey: ['ships-automation'],
    queryFn: () => shipsApi.getAll(),
    staleTime: 60000, 
  });

  const createShipMut = useMutation({
    mutationFn: (data: Record<string, unknown>) => shipsApi.create(data),
    onSuccess: () => {
      // Invalidate both automation key and general ships key if it exists
      queryClient.invalidateQueries({ queryKey: ['ships'] });
      queryClient.invalidateQueries({ queryKey: ['ships-automation'] });
    },
  });

  useEffect(() => {
    if (!enabled) return;
    if (isSuccess && Array.isArray(ships) && !automationTriggered.current) {
      automationTriggered.current = true;
      const existingNames = new Set(ships.map((s: any) => s.name));
      
      REAL_SHIP_NAMES.forEach((name, index) => {
        if (!existingNames.has(name)) {
          // Generate mock data for the real ship
          const now = new Date();
          // Stagger arrival times
          now.setHours(now.getHours() + index); 
          
          createShipMut.mutate({
            imoNumber: `IMO900${Math.floor(1000 + Math.random() * 9000)}${index}`,
            name: name,
            arrivalTime: now.toISOString(),
            expectedDeparture: new Date(now.getTime() + 48 * 60 * 60 * 1000).toISOString(),
            containerCount: 15000 + (index * 2000),
            priority: index === 1 ? 'HIGH' : 'MEDIUM',
            cargoType: 'Container',
            shipLength: 350 + (index * 20),
            shipWidth: 50 + (index * 5),
            draft: 14 + (index % 3)
          });
        }
      });
    }
  }, [isSuccess, ships]);
}
