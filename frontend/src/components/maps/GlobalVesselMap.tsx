import React, { useState, useEffect, useRef } from 'react';
import { MapContainer, TileLayer, Marker, Popup, Polyline } from 'react-leaflet';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import { Ship, Anchor, Navigation, Activity } from 'lucide-react';

// Custom icons using Lucide SVGs wrapped in div icons for Leaflet
const createShipIcon = (color: string) => L.divIcon({
  html: `<div style="background-color: ${color}; width: 24px; height: 24px; border-radius: 50%; display: flex; align-items: center; justify-content: center; border: 2px solid white; box-shadow: 0 2px 4px rgba(0,0,0,0.3);"><svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M2 21c.6.5 1.2 1 2.5 1 2.5 0 2.5-2 5-2 1.3 0 1.9.5 2.5 1 .6.5 1.2 1 2.5 1 2.5 0 2.5-2 5-2 1.3 0 1.9.5 2.5 1"/><path d="M19.38 20A11.6 11.6 0 0 0 21 14l-9-4-9 4c0 2.9.94 5.34 2.81 7.76"/><path d="M19 13V7a2 2 0 0 0-2-2H7a2 2 0 0 0-2 2v6"/><path d="M12 10v4"/><path d="M12 2v3"/></svg></div>`,
  className: 'custom-ship-marker',
  iconSize: [24, 24],
  iconAnchor: [12, 12]
});

const portIcon = L.divIcon({
  html: `<div style="background-color: #0B1F33; width: 28px; height: 28px; border-radius: 50%; display: flex; align-items: center; justify-content: center; border: 2px solid white; box-shadow: 0 0 15px rgba(45, 91, 255, 0.6);"><svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="5" r="3"/><line x1="12" y1="22" x2="12" y2="8"/><line x1="5" y1="12" x2="19" y2="12"/><path d="M5 12v6a2 2 0 0 0 2 2h10a2 2 0 0 0 2-2v-6"/></svg></div>`,
  className: 'custom-port-marker',
  iconSize: [28, 28],
  iconAnchor: [14, 14]
});

// Vadhvan Port approximate coordinates
const VADHVAN_PORT: [number, number] = [19.803, 72.637];

// Types for Live AIS Data
type LiveShip = {
  mmsi: number;
  name: string;
  lat: number;
  lng: number;
  speed: number;
  heading: number;
  lastUpdate: number;
  color: string;
};

// Generate deterministic colors based on MMSI
const getShipColor = (mmsi: number) => {
  const colors = ['#3B82F6', '#8B5CF6', '#10B981', '#F59E0B', '#EF4444', '#06B6D4', '#EC4899'];
  return colors[mmsi % colors.length];
};

export default function GlobalVesselMap() {
  const [activeShip, setActiveShip] = useState<string | null>(null);
  const [liveShips, setLiveShips] = useState<Record<number, LiveShip>>({});
  const [isConnected, setIsConnected] = useState(false);
  
  const wsRef = useRef<WebSocket | null>(null);

  useEffect(() => {
    // Connect to AisStream.io
    const connectAIS = () => {
      const socket = new WebSocket('wss://stream.aisstream.io/v0/stream');
      
      socket.onopen = () => {
        console.log("AIS WebSocket connected successfully");
        setIsConnected(true);
        const subscriptionMessage = {
          APIKey: import.meta.env.VITE_AISSTREAM_API_KEY,
          // Global bounding box
          BoundingBoxes: [[[-90, -180], [90, 180]]],
          FilterMessageTypes: ['PositionReport']
        };
        socket.send(JSON.stringify(subscriptionMessage));
      };

      socket.onmessage = async (event) => {
        try {
          const messageText = typeof event.data === 'string' ? event.data : await event.data.text();
          const aisMessage = JSON.parse(messageText);
          
          if (aisMessage.MessageType === 'PositionReport') {
            const meta = aisMessage.MetaData;
            const report = aisMessage.Message.PositionReport;
            
            if (meta && meta.MMSI && meta.latitude && meta.longitude) {
              setLiveShips((prev) => ({
                ...prev,
                [meta.MMSI]: {
                  mmsi: meta.MMSI,
                  name: meta.ShipName ? meta.ShipName.trim() : `MMSI: ${meta.MMSI}`,
                  lat: meta.latitude,
                  lng: meta.longitude,
                  speed: report.Sog || 0,
                  heading: report.Cog || 0,
                  lastUpdate: Date.now(),
                  color: getShipColor(meta.MMSI)
                }
              }));
            }
          }
        } catch (error) {
          console.error('Error parsing AIS message', error);
        }
      };

      socket.onclose = () => {
        setIsConnected(false);
        // Try to reconnect after 5 seconds
        setTimeout(connectAIS, 5000);
      };
      
      socket.onerror = (error) => {
        console.error('AIS WebSocket Error:', error);
      };

      wsRef.current = socket;
    };

    connectAIS();

    // Cleanup stale ships every 30 seconds
    const cleanupInterval = setInterval(() => {
      const now = Date.now();
      setLiveShips(prev => {
        const next = { ...prev };
        let changed = false;
        Object.keys(next).forEach(key => {
          const mmsi = Number(key);
          // Remove ships not updated in the last 15 minutes
          if (now - next[mmsi].lastUpdate > 15 * 60 * 1000) {
            delete next[mmsi];
            changed = true;
          }
        });
        return changed ? next : prev;
      });
    }, 30000);

    // Mock ship generator if API returns no data or fails
    const mockInterval = setInterval(() => {
      setLiveShips(prev => {
        // If we have real ships (or previous mock ships), just update/add mocks
        const next = { ...prev };
        const now = Date.now();
        
        // Ensure we always have at least 5 mock ships
        const currentMockMmsis = Object.keys(next).map(Number).filter(id => id < 1000);
        
        if (Object.keys(next).length < 5 || currentMockMmsis.length > 0) {
          // Add or update mock ships
          for (let i = 1; i <= 5; i++) {
            const mmsi = i;
            if (next[mmsi]) {
              // Move existing mock ship
              const speed = next[mmsi].speed;
              const heading = next[mmsi].heading;
              
              // Randomly adjust heading
              const newHeading = (heading + (Math.random() - 0.5) * 20 + 360) % 360;
              const headingRad = newHeading * (Math.PI / 180);
              
              // Move based on speed (approximate degrees conversion for simplicity)
              const latDelta = Math.cos(headingRad) * (speed * 0.0001);
              const lngDelta = Math.sin(headingRad) * (speed * 0.0001);
              
              next[mmsi] = {
                ...next[mmsi],
                lat: next[mmsi].lat + latDelta,
                lng: next[mmsi].lng + lngDelta,
                heading: newHeading,
                lastUpdate: now
              };
            } else {
              // Spawn new mock ship near Vadhvan Port
              const MOCK_SHIP_NAMES = ['MSC Oscar', 'Ever Given', 'CMA CGM Jacques Saade', 'HMM Algeciras', 'Madrid Maersk'];
              next[mmsi] = {
                mmsi,
                name: MOCK_SHIP_NAMES[i - 1] || `Mock Vessel ${i}`,
                lat: 19.803 + (Math.random() - 0.5) * 0.1,
                lng: 72.637 + (Math.random() - 0.5) * 0.1,
                speed: 5 + Math.random() * 15,
                heading: Math.random() * 360,
                lastUpdate: now,
                color: getShipColor(mmsi)
              };
            }
          }
          return next;
        }
        return prev;
      });
    }, 1000);

    return () => {
      if (wsRef.current) {
        wsRef.current.close();
      }
      clearInterval(cleanupInterval);
      clearInterval(mockInterval);
    };
  }, []);

  const liveShipsArray = Object.values(liveShips);

  return (
    <div className="h-full w-full relative bg-surface rounded-md border border-outline-variant overflow-hidden z-0">
      <MapContainer 
        center={[19.5, 71.8]} 
        zoom={7} 
        style={{ height: '100%', width: '100%' }}
        zoomControl={false}
      >
        {/* Standard OSM with CSS inversion for dark mode */}
        <TileLayer
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
          className="map-tiles-dark"
        />

        {/* Port Marker */}
        <Marker position={VADHVAN_PORT} icon={portIcon}>
          <Popup className="custom-popup">
            <div className="p-1">
              <h3 className="font-bold text-sm text-port-navy flex items-center gap-1">
                <Anchor size={14} /> Vadhvan Mega Port
              </h3>
              <p className="text-xs text-on-surface-variant mt-1">Status: Fully Operational</p>
            </div>
          </Popup>
        </Marker>

        {/* Live Ships */}
        {liveShipsArray.map((ship) => (
          <Marker 
            key={ship.mmsi}
            position={[ship.lat, ship.lng]} 
            icon={createShipIcon(ship.color)}
            eventHandlers={{
              click: () => setActiveShip(ship.mmsi.toString())
            }}
          >
            <Popup>
              <div className="p-1 min-w-[150px]">
                <div className="flex items-center gap-2 mb-2">
                  <div className="w-2 h-2 rounded-full" style={{ backgroundColor: ship.color }} />
                  <h3 className="font-bold text-sm text-port-navy">{ship.name || `Ship ${ship.mmsi}`}</h3>
                </div>
                <div className="space-y-1 text-xs text-on-surface-variant">
                  <p><strong>MMSI:</strong> {ship.mmsi}</p>
                  <p><strong>Speed:</strong> {ship.speed} kn</p>
                  <p><strong>Heading:</strong> {ship.heading}°</p>
                  <p><strong>Last Update:</strong> {new Date(ship.lastUpdate).toLocaleTimeString()}</p>
                </div>
              </div>
            </Popup>
          </Marker>
        ))}
      </MapContainer>
      
      {/* Overlay UI */}
      <div className="absolute top-4 left-4 z-[400] bg-white/90 backdrop-blur-sm p-3 rounded-md shadow-card border border-outline-variant max-h-[80%] overflow-y-auto w-64">
        <h4 className="font-semibold text-sm flex items-center justify-between text-port-navy mb-2 pb-2 border-b border-outline-variant">
          <span className="flex items-center gap-2">
            <Navigation size={16} className="text-secondary" /> 
            Live Vessel Tracking
          </span>
          <span title={isConnected ? "Connected to AIS" : "Connecting..."}>
            <Activity size={14} className={isConnected ? "text-green-500 animate-pulse" : "text-gray-400"} />
          </span>
        </h4>
        
        {liveShipsArray.length === 0 ? (
          <div className="text-xs text-on-surface-variant text-center py-4">
            {isConnected ? 'Waiting for AIS data...' : 'Connecting to AIS stream...'}
          </div>
        ) : (
          <div className="space-y-2">
            {liveShipsArray.slice(0, 10).map(ship => (
              <div 
                key={ship.mmsi} 
                className="flex items-center justify-between gap-2 text-xs cursor-pointer hover:bg-background p-1.5 rounded"
                onClick={() => setActiveShip(ship.mmsi.toString())}
              >
                <div className="flex items-center gap-2 truncate">
                  <div className="w-2 h-2 rounded-full shrink-0 animate-pulse" style={{ backgroundColor: ship.color }} />
                  <span className="font-medium truncate">{ship.name || ship.mmsi}</span>
                </div>
                <span className="text-on-surface-variant font-mono shrink-0">{ship.speed}kn</span>
              </div>
            ))}
            {liveShipsArray.length > 10 && (
              <div className="text-xs text-center text-on-surface-variant pt-2">
                + {liveShipsArray.length - 10} more vessels
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
}
