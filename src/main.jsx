import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App.jsx";
import { BrowserRouter } from "react-router-dom";
import { TravelProvider } from "./context/TravelContext.jsx";
import './index.css';
import './style.css';

ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <BrowserRouter>
      <TravelProvider>
        <App />
      </TravelProvider>
    </BrowserRouter>
  </React.StrictMode>
);
