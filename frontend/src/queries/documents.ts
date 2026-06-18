import { useQuery } from '@tanstack/vue-query';
import { api } from '../api/client';

export function useDocuments() {
  return useQuery({
    queryKey: ['documents'],
    queryFn: () => api.get('/documents').then((r) => r.data),
  });
}
