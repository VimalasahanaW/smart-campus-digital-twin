import { useEffect, useRef, useState } from 'react';

export function usePolling(fetchFn, intervalMs = 7000, deps = []) {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const savedFetchFn = useRef(fetchFn);
  savedFetchFn.current = fetchFn;

  useEffect(() => {
    let cancelled = false;

    const run = () => {
      savedFetchFn.current()
        .then(result => {
          if (!cancelled) {
            setData(result);
            setError(null);
          }
        })
        .catch(err => {
          if (!cancelled) setError(err.message);
        })
        .finally(() => {
          if (!cancelled) setLoading(false);
        });
    };

    run(); // fetch immediately on mount
    const id = setInterval(run, intervalMs);

    return () => {
      cancelled = true;
      clearInterval(id);
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, deps);

  return { data, loading, error };
}