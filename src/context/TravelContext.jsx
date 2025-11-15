import { createContext, useContext, useState } from "react";

const TravelContext = createContext();

export function TravelProvider({ children }) {
  const [region, setRegion] = useState(null);
  const [cities, setCities] = useState([]);
  const [foodPreferences, setFoodPreferences] = useState([]);
  const [categories, setCategories] = useState([]);

  return (
    <TravelContext.Provider value={{
      region,
      setRegion,
      cities,
      setCities,
      foodPreferences,
      setFoodPreferences,
      categories,
      setCategories
    }}>
      {children}
    </TravelContext.Provider>
  );
}

export function useTravel() {
  return useContext(TravelContext);
}
