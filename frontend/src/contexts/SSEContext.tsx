import React, { createContext, useContext, useState, useEffect, useRef, useCallback } from 'react';
import { useQueryClient } from '@tanstack/react-query';
import { Notification } from '@/types';

interface SSEContextType {
  notifications: Notification[];
  unreadCount: number;
  isConnected: boolean;
  markAsRead: (id: string) => void;
  markAllRead: () => void;
  addNotification: (n: Notification) => void;
}

const SSEContext = createContext<SSEContextType | null>(null);

type SSEEvent = {
  type: string;
  data?: unknown;
};

export const SSEProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [notifications, setNotifications] = useState<Notification[]>([]);
  const [isConnected, setIsConnected] = useState(false);
  const eventSourceRef = useRef<EventSource | null>(null);

  const queryClient = useQueryClient();

  const handleEvent = useCallback((event: MessageEvent) => {
    try {
      const parsed: SSEEvent = JSON.parse(event.data);
      if (parsed.type === 'notification' && parsed.data) {
        const n = parsed.data as Notification;
        setNotifications(prev => [n, ...prev].slice(0, 50)); // keep last 50
      }
    } catch {
      // ignore parse errors
    }
  }, []);

  useEffect(() => {
    const token = localStorage.getItem('accessToken');
    if (!token) return;

    const sseUrl = `${import.meta.env.VITE_API_URL || '/api'}/events?token=${token}`;
    const es = new EventSource(sseUrl);
    eventSourceRef.current = es;

    es.onopen = () => setIsConnected(true);
    es.onmessage = handleEvent;
    
    // Listen to specific data update events
    const invalidate = (queryKey: string) => () => queryClient.invalidateQueries({ queryKey: [queryKey] });
    
    es.addEventListener('vehicle_update', invalidate('vehicles'));
    es.addEventListener('driver_update', invalidate('drivers'));
    es.addEventListener('trip_update', invalidate('trips'));
    es.addEventListener('container_update', invalidate('containers'));
    es.addEventListener('ship_update', invalidate('ships'));
    es.addEventListener('dock_update', invalidate('docks'));
    es.addEventListener('dashboard_update', invalidate('dashboard'));
    es.addEventListener('maintenance_alert', invalidate('maintenance'));

    es.onerror = () => {
      setIsConnected(false);
      // auto-reconnect handled by EventSource
    };

    return () => {
      es.close();
      setIsConnected(false);
    };
  }, [handleEvent, queryClient]);

  const markAsRead = (id: string) => {
    setNotifications(prev =>
      prev.map(n => (n.id === id ? { ...n, read: true } : n))
    );
  };

  const markAllRead = () => {
    setNotifications(prev => prev.map(n => ({ ...n, read: true })));
  };

  const addNotification = (n: Notification) => {
    setNotifications(prev => [n, ...prev].slice(0, 50));
  };

  const unreadCount = notifications.filter(n => !n.read).length;

  return (
    <SSEContext.Provider
      value={{ notifications, unreadCount, isConnected, markAsRead, markAllRead, addNotification }}
    >
      {children}
    </SSEContext.Provider>
  );
};

export const useSSE = (): SSEContextType => {
  const context = useContext(SSEContext);
  if (!context) throw new Error('useSSE must be used within SSEProvider');
  return context;
};
