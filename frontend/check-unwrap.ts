import { z } from 'zod';
const unwrap = async (promise: Promise<any>) => {
  const { data } = await promise;
  return data;
};
