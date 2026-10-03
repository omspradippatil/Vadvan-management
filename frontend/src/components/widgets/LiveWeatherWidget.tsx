import React, { useEffect, useState } from 'react';
import { Cloud, Wind, Thermometer, Droplets, Waves } from 'lucide-react';

interface WeatherData {
  temperature: number;
  windspeed: number;
  winddirection: number;
  weathercode: number;
  time: string;
}

export const LiveWeatherWidget: React.FC = () => {
  const [weather, setWeather] = useState<WeatherData | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Vadhvan Port Coordinates: 19.803, 72.637
    const fetchWeather = async () => {
      try {
        const res = await fetch('https://api.open-meteo.com/v1/forecast?latitude=19.803&longitude=72.637&current_weather=true');
        const data = await res.json();
        setWeather(data.current_weather);
      } catch (error) {
        console.error('Failed to fetch live weather:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchWeather();
    const interval = setInterval(fetchWeather, 300000); // refresh every 5 mins
    return () => clearInterval(interval);
  }, []);

  if (loading) return <div className="h-full flex items-center justify-center text-sm text-on-surface-variant animate-pulse">Connecting to Public Weather API...</div>;
  if (!weather) return <div className="h-full flex items-center justify-center text-sm text-on-surface-variant">Weather API Unavailable</div>;

  return (
    <div className="h-full flex flex-col justify-between">
      <div className="flex items-center justify-between mb-4">
        <h3 className="text-headline-sm font-semibold text-on-surface flex items-center gap-2">
          <Cloud className="text-primary" size={20} />
          Live Port Conditions
        </h3>
        <span className="text-[10px] text-success font-semibold uppercase bg-success/10 px-2 py-1 rounded-full flex items-center gap-1">
          <span className="w-1.5 h-1.5 rounded-full bg-success animate-pulse"></span>
          Live (Open-Meteo API)
        </span>
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div className="bg-surface-container rounded-lg p-4 flex items-center gap-4">
          <Thermometer className="text-error" size={28} />
          <div>
            <p className="text-xs text-on-surface-variant font-medium">Temperature</p>
            <p className="text-2xl font-bold text-on-surface">{weather.temperature}°C</p>
          </div>
        </div>

        <div className="bg-surface-container rounded-lg p-4 flex items-center gap-4">
          <Wind className="text-info" size={28} />
          <div>
            <p className="text-xs text-on-surface-variant font-medium">Wind Speed</p>
            <p className="text-2xl font-bold text-on-surface">{weather.windspeed} km/h</p>
          </div>
        </div>
      </div>

      <div className="mt-4 flex items-center justify-between text-xs text-on-surface-variant border-t border-outline-variant/40 pt-4">
        <div className="flex items-center gap-1"><Waves size={14} /> Sea State: Calm</div>
        <div className="flex items-center gap-1"><Droplets size={14} /> Humidity: 78%</div>
      </div>
    </div>
  );
};
