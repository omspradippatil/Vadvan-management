import { useShipAutomation } from './useShipAutomation';
import { useMaintenanceFuelAutomation } from './useMaintenanceFuelAutomation';
import { useRailTripAutomation } from './useRailTripAutomation';

export function usePortAutomation(enabled = true) {
  useShipAutomation(enabled);
  useMaintenanceFuelAutomation(enabled);
  useRailTripAutomation(enabled);
}
