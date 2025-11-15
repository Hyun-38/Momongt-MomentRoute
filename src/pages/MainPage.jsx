import React, { useState } from "react";
import { useLocation } from "react-router-dom";
import Header from "../components/Header/Header.jsx";
import TravelPlanner from "../components/TravelPlanner/TravelPlanner.jsx";
import SelectedRouteCard from "../components/Route/Route.jsx";
import FestivalSection from "../components/FestivalSection/FestivalSection.jsx";
import RegionRecommend from "../components/RegionRecommend/RegionRecommendSection.jsx";
import SaveTripModal from "../components/SaveModal/SaveTripModal.jsx";

export default function MainPage() {
  const [showModal, setShowModal] = useState(false);
  const location = useLocation();

  const {
    destination = "",
    stops: initialStops = [],
    food = [],
    categories = [],
  } = location.state || {};

  // ⭐ stops를 상태로 관리
  const [stops, setStops] = useState(initialStops);

  const cities = destination ? [...stops, destination] : [];

  return (
    <div>
      <Header />

      <TravelPlanner
        destination={destination}
        stops={stops}
        removeStop={(city) =>
          setStops((prev) => prev.filter((c) => c !== city))
        }
      />

      <SelectedRouteCard
        cities={cities}
        foodPreferences={food}
        categories={categories}
        onSave={() => setShowModal(true)}
      />

      <FestivalSection cities={cities} />
      <RegionRecommend cities={cities} />

      {showModal && (
        <SaveTripModal onClose={() => setShowModal(false)} />
      )}
    </div>
  );
}